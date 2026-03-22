package org.mesdag.portlib.attachment;

import com.google.common.base.Predicates;
import com.mojang.serialization.Codec;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.network.PortRegistryFriendlyByteBuf;
import org.mesdag.portlib.network.codec.PortStreamCodec;
import org.mesdag.portlib.wrapper.IPortNBTSerializable;

import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class PortAttachmentType<T> {
    private final AttachmentType<T> delegate;

    private PortAttachmentType(AttachmentType<T> delegate) {
        this.delegate = delegate;
    }

    public static <T> PortBuilder<T> builder(Supplier<T> defaultValueSupplier) {
        return new PortBuilder<>(AttachmentType.builder(defaultValueSupplier));
    }

    public static <T> PortBuilder<T> builder(Function<IAttachmentHolder, T> defaultValueConstructor) {
        return new PortBuilder<>(AttachmentType.builder(defaultValueConstructor));
    }

    public static <S extends Tag, T extends IPortNBTSerializable<S>> PortBuilder<T> serializable(Supplier<T> defaultValueSupplier) {
        return new PortBuilder<>(AttachmentType.serializable(defaultValueSupplier));
    }

    public static <S extends Tag, T extends IPortNBTSerializable<S>> PortBuilder<T> serializable(Function<IAttachmentHolder, T> defaultValueConstructor) {
        return new PortBuilder<>(AttachmentType.serializable(defaultValueConstructor));
    }

    @Diff
    public AttachmentType<T> unwrap() {
        return delegate;
    }

    @Diff
    public static <T> PortAttachmentType<T> wrap(AttachmentType<T> delegate) {
        return new PortAttachmentType<>(delegate);
    }

    public static class PortBuilder<T> {
        private final AttachmentType.Builder<T> builder;

        private PortBuilder(AttachmentType.Builder<T> builder) {
            this.builder = builder;
        }

        public PortBuilder<T> serialize(IPortAttachmentSerializer<?, T> serializer) {
            builder.serialize(serializer.unwrap());
            return this;
        }

        public PortBuilder<T> serialize(Codec<T> codec) {
            return serialize(codec, Predicates.alwaysTrue());
        }

        public PortBuilder<T> serialize(Codec<T> codec, Predicate<? super T> shouldSerialize) {
            builder.serialize(codec, shouldSerialize);
            return this;
        }

        public PortBuilder<T> copyOnDeath() {
            builder.copyOnDeath();
            return this;
        }

        public PortBuilder<T> copyHandler(IPortAttachmentCopyHandler<T> cloner) {
            builder.copyHandler(cloner.unwrap());
            return this;
        }

        public PortBuilder<T> sync(PortAttachmentSyncHandler<T> syncHandler) {
            builder.sync(syncHandler.unwrap());
            return this;
        }

        public PortBuilder<T> sync(PortStreamCodec<? super PortRegistryFriendlyByteBuf, T> streamCodec) {
            return sync((holder, to) -> true, streamCodec);
        }

        public PortBuilder<T> sync(BiPredicate<IPortAttachmentHolder, ServerPlayer> sendToPlayer, PortStreamCodec<? super PortRegistryFriendlyByteBuf, T> streamCodec) {
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
            return wrap(builder.build());
        }
    }
}
