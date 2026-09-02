package org.mesdag.portlib.attachment;

import PortLib.extensions.com.mojang.serialization.DataResult.PortDataResultExtension;
import com.google.common.base.Predicates;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.network.PortRegistryFriendlyByteBuf;
import org.mesdag.portlib.network.codec.PortStreamCodec;
import org.mesdag.portlib.util.Protected;
import org.mesdag.portlib.wrapper.IPortNBTSerializable;
import org.mesdag.portlib.wrapper.common.extensions.IPortHolderLookupProviderExtension;

import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class PortAttachmentType<T> {
    @Protected
    public final Function<IPortAttachmentHolder, T> defaultValueSupplier;
    @Protected
    public final @Nullable IPortAttachmentSerializer<?, T> serializer;
    @Protected
    public final boolean copyOnDeath;
    @Protected
    public final IPortAttachmentCopyHandler<T> copyHandler;
    @Protected
    public @Nullable PortAttachmentSyncHandler<T> syncHandler;

    private PortAttachmentType(Builder<T> builder) {
        this.defaultValueSupplier = builder.defaultValueSupplier;
        this.serializer = builder.serializer;
        this.copyOnDeath = builder.copyOnDeath;
        this.copyHandler = builder.copyHandler != null ? builder.copyHandler : defaultCopyHandler(serializer);
        this.syncHandler = builder.syncHandler;
    }

    private static <T, H extends Tag> IPortAttachmentCopyHandler<T> defaultCopyHandler(@Nullable IPortAttachmentSerializer<H, T> serializer) {
        if (serializer == null) {
            return (attachment, holder, provider) -> {
                throw new UnsupportedOperationException("Cannot copy non-serializable attachments");
            };
        }
        return (attachment, holder, provider) -> {
            H serialized = serializer.write(attachment, provider);
            if (serialized != null) {
                return serializer.read(holder, serialized, provider);
            }
            return null;
        };
    }

    public static <T> Builder<T> builder(Supplier<T> defaultValueSupplier) {
        return builder(holder -> defaultValueSupplier.get());
    }

    public static <T> Builder<T> builder(Function<IPortAttachmentHolder, T> defaultValueConstructor) {
        return new Builder<>(defaultValueConstructor);
    }

    public static <S extends Tag, T extends IPortNBTSerializable<S>> Builder<T> serializable(Supplier<T> defaultValueSupplier) {
        return serializable(holder -> defaultValueSupplier.get());
    }

    public static <S extends Tag, T extends IPortNBTSerializable<S>> Builder<T> serializable(Function<IPortAttachmentHolder, T> defaultValueConstructor) {
        return builder(defaultValueConstructor).serialize(new IPortAttachmentSerializer<S, T>() {
            @Override
            public T read(IPortAttachmentHolder holder, S tag, HolderLookup.Provider provider) {
                var ret = defaultValueConstructor.apply(holder);
                ret.deserializeNBT(provider, tag);
                return ret;
            }

            @Nullable
            @Override
            public S write(T attachment, HolderLookup.Provider provider) {
                return attachment.serializeNBT(provider);
            }
        });
    }

    public static class Builder<T> {
        private final Function<IPortAttachmentHolder, T> defaultValueSupplier;
        @Nullable
        private IPortAttachmentSerializer<?, T> serializer;
        private boolean copyOnDeath;
        @Nullable
        private IPortAttachmentCopyHandler<T> copyHandler;
        @Nullable
        private PortAttachmentSyncHandler<T> syncHandler;

        private Builder(Function<IPortAttachmentHolder, T> defaultValueSupplier) {
            this.defaultValueSupplier = defaultValueSupplier;
        }

        public Builder<T> serialize(IPortAttachmentSerializer<?, T> serializer) {
            Objects.requireNonNull(serializer);
            if (this.serializer != null) {
                throw new IllegalStateException("Serializer already set");
            }
            this.serializer = serializer;
            return this;
        }

        public Builder<T> serialize(Codec<T> codec) {
            return serialize(codec, Predicates.alwaysTrue());
        }

        public Builder<T> serialize(Codec<T> codec, Predicate<? super T> shouldSerialize) {
            Objects.requireNonNull(codec);
            return serialize(new IPortAttachmentSerializer<>() {
                @Override
                public T read(IPortAttachmentHolder holder, Tag tag, HolderLookup.Provider provider) {
                    DataResult<T> parsingResult = codec.parse(IPortHolderLookupProviderExtension.of(provider).createSerializationContext(NbtOps.INSTANCE), tag);
                    return PortDataResultExtension.getOrThrow(parsingResult, msg -> buildException("read", msg));
                }

                @Nullable
                @Override
                public Tag write(T attachment, HolderLookup.Provider provider) {
                    if (!shouldSerialize.test(attachment)) {
                        return null;
                    }
                    DataResult<Tag> encodingResult = codec.encodeStart(IPortHolderLookupProviderExtension.of(provider).createSerializationContext(NbtOps.INSTANCE), attachment);
                    return PortDataResultExtension.getOrThrow(encodingResult, msg -> buildException("write", msg));
                }

                private IllegalStateException buildException(String operation, String error) {
                    return new IllegalStateException("Unable to " + operation + " attachment due to an internal codec error: " + error);
                }
            });
        }

        public Builder<T> copyOnDeath() {
            if (serializer == null) {
                throw new IllegalStateException("copyOnDeath requires a serializer");
            }
            this.copyOnDeath = true;
            return this;
        }

        public Builder<T> copyHandler(IPortAttachmentCopyHandler<T> cloner) {
            Objects.requireNonNull(cloner);
            if (this.serializer == null) {
                throw new IllegalStateException("copyHandler requires a serializer");
            }
            this.copyHandler = cloner;
            return this;
        }

        public Builder<T> sync(PortAttachmentSyncHandler<T> syncHandler) {
            Objects.requireNonNull(syncHandler);
            this.syncHandler = syncHandler;
            return this;
        }

        public Builder<T> sync(PortStreamCodec<? super PortRegistryFriendlyByteBuf, T> streamCodec) {
            return sync((holder, to) -> true, streamCodec);
        }

        public Builder<T> sync(BiPredicate<IPortAttachmentHolder, ServerPlayer> sendToPlayer, PortStreamCodec<? super PortRegistryFriendlyByteBuf, T> streamCodec) {
            Objects.requireNonNull(sendToPlayer);
            Objects.requireNonNull(streamCodec);
            return sync(new PortAttachmentSyncHandler<>() {
                @Override
                public boolean sendToPlayer(IPortAttachmentHolder holder, ServerPlayer to) {
                    return sendToPlayer.test(holder, to);
                }

                @Override
                public void write(PortRegistryFriendlyByteBuf buf, T attachment, boolean initialSync) {
                    streamCodec.encode(buf, attachment);
                }

                @Override
                public T read(IPortAttachmentHolder holder, PortRegistryFriendlyByteBuf buf, @Nullable T previousValue) {
                    return streamCodec.decode(buf);
                }
            });
        }

        public PortAttachmentType<T> build() {
            return new PortAttachmentType<>(this);
        }
    }
}
