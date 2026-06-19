package org.mesdag.portlib.network.codec;

import PortLib.extensions.com.mojang.serialization.DataResult.PortDataResultExtension;
import PortLib.extensions.net.minecraft.core.HolderLookup.PortHolderLookupExtension;
import PortLib.extensions.net.minecraft.core.IdMap.PortIdMapExtension;
import PortLib.extensions.net.minecraft.resources.ResourceLocation.PortResourceLocationExtension;
import PortLib.extensions.net.minecraftforge.registries.ForgeRegistry.PortForgeRegistryExtension;
import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.holdersets.HolderSetType;
import net.minecraftforge.registries.holdersets.ICustomHolderSet;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.mesdag.portlib.diff.PortRegistryManager;
import org.mesdag.portlib.network.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

public interface PortByteBufCodecs {
    PortStreamCodec<ByteBuf, Boolean> BOOL = new PortStreamCodec<>() {
        public Boolean decode(ByteBuf buffer) {
            return buffer.readBoolean();
        }

        public void encode(ByteBuf buffer, Boolean value) {
            buffer.writeBoolean(value);
        }
    };
    PortStreamCodec<ByteBuf, Byte> BYTE = new PortStreamCodec<>() {
        public Byte decode(ByteBuf buffer) {
            return buffer.readByte();
        }

        public void encode(ByteBuf buffer, Byte value) {
            buffer.writeByte(value);
        }
    };
    PortStreamCodec<ByteBuf, Short> SHORT = new PortStreamCodec<>() {
        public Short decode(ByteBuf buffer) {
            return buffer.readShort();
        }

        public void encode(ByteBuf buffer, Short value) {
            buffer.writeShort(value);
        }
    };
    PortStreamCodec<ByteBuf, Integer> UNSIGNED_SHORT = new PortStreamCodec<>() {
        public Integer decode(ByteBuf buffer) {
            return buffer.readUnsignedShort();
        }

        public void encode(ByteBuf buffer, Integer value) {
            buffer.writeShort(value);
        }
    };
    PortStreamCodec<ByteBuf, Integer> INT = new PortStreamCodec<>() {
        public Integer decode(ByteBuf buffer) {
            return buffer.readInt();
        }

        public void encode(ByteBuf buffer, Integer value) {
            buffer.writeInt(value);
        }
    };
    PortStreamCodec<ByteBuf, Integer> VAR_INT = new PortStreamCodec<>() {
        public Integer decode(ByteBuf buffer) {
            return PortVarInt.read(buffer);
        }

        public void encode(ByteBuf buffer, Integer value) {
            PortVarInt.write(buffer, value);
        }
    };
    PortStreamCodec<ByteBuf, Long> VAR_LONG = new PortStreamCodec<>() {
        public Long decode(ByteBuf buffer) {
            return PortVarLong.read(buffer);
        }

        public void encode(ByteBuf buffer, Long value) {
            PortVarLong.write(buffer, value);
        }
    };
    PortStreamCodec<ByteBuf, Float> FLOAT = new PortStreamCodec<>() {
        public Float decode(ByteBuf buffer) {
            return buffer.readFloat();
        }

        public void encode(ByteBuf buffer, Float value) {
            buffer.writeFloat(value);
        }
    };
    PortStreamCodec<ByteBuf, Double> DOUBLE = new PortStreamCodec<>() {
        public Double decode(ByteBuf buffer) {
            return buffer.readDouble();
        }

        public void encode(ByteBuf buffer, Double value) {
            buffer.writeDouble(value);
        }
    };
    PortStreamCodec<ByteBuf, byte[]> BYTE_ARRAY = new PortStreamCodec<>() {
        public byte[] decode(ByteBuf buffer) {
            return PortFriendlyByteBuf.readByteArray(buffer);
        }

        public void encode(ByteBuf buffer, byte[] value) {
            PortFriendlyByteBuf.writeByteArray(buffer, value);
        }
    };
    PortStreamCodec<FriendlyByteBuf, byte[]> UNBOUNDED_BYTE_ARRAY = new PortStreamCodec<>() {
        @Override
        public byte[] decode(FriendlyByteBuf buffer) {
            return buffer.readByteArray();
        }

        @Override
        public void encode(FriendlyByteBuf buf, byte[] value) {
            buf.writeByteArray(value);
        }
    };
    PortStreamCodec<ByteBuf, String> STRING_UTF8 = stringUtf8(32767);
    PortStreamCodec<ByteBuf, Tag> TAG = tagCodec(() -> new NbtAccounter(2097152L));
    PortStreamCodec<ByteBuf, Tag> TRUSTED_TAG = tagCodec(() -> new NbtAccounter(Long.MAX_VALUE));
    PortStreamCodec<ByteBuf, CompoundTag> COMPOUND_TAG = compoundTagCodec(() -> new NbtAccounter(2097152L));
    PortStreamCodec<ByteBuf, CompoundTag> TRUSTED_COMPOUND_TAG = compoundTagCodec(() -> new NbtAccounter(Long.MAX_VALUE));
    PortStreamCodec<ByteBuf, Optional<CompoundTag>> OPTIONAL_COMPOUND_TAG = new PortStreamCodec<>() {
        public Optional<CompoundTag> decode(ByteBuf value) {
            return Optional.ofNullable(PortFriendlyByteBuf.readNbt(value));
        }

        public void encode(ByteBuf buffer, Optional<CompoundTag> value) {
            PortFriendlyByteBuf.writeNbt(buffer, value.orElse(null));
        }
    };
    PortStreamCodec<ByteBuf, Vector3f> VECTOR3F = new PortStreamCodec<>() {
        public Vector3f decode(ByteBuf buffer) {
            return PortFriendlyByteBuf.readVector3f(buffer);
        }

        public void encode(ByteBuf buffer, Vector3f value) {
            PortFriendlyByteBuf.writeVector3f(buffer, value);
        }
    };
    PortStreamCodec<ByteBuf, Vector4f> VECTOR4F = new PortStreamCodec<>() {
        public Vector4f decode(ByteBuf buffer) {
            return PortFriendlyByteBuf.readVector4f(buffer);
        }

        public void encode(ByteBuf buffer, Vector4f value) {
            PortFriendlyByteBuf.writeVector4f(buffer, value);
        }
    };
    PortStreamCodec<FriendlyByteBuf, UUID> UUID = new PortStreamCodec<>() {
        @Override
        public UUID decode(FriendlyByteBuf buffer) {
            return buffer.readUUID();
        }

        @Override
        public void encode(FriendlyByteBuf buffer, UUID value) {
            buffer.writeUUID(value);
        }
    };
    PortStreamCodec<FriendlyByteBuf, BlockPos> BLOCK_POS = new PortStreamCodec<>() {
        @Override
        public BlockPos decode(FriendlyByteBuf buffer) {
            return buffer.readBlockPos();
        }

        @Override
        public void encode(FriendlyByteBuf buffer, BlockPos value) {
            buffer.writeBlockPos(value);
        }
    };

    static <B extends ByteBuf, K, V, M extends Map<K, V>> PortStreamCodec<B, M> map(
            IntFunction<? extends M> factory, PortStreamCodec<? super B, K> keyCodec, PortStreamCodec<? super B, V> valueCodec
    ) {
        return map(factory, keyCodec, valueCodec, Integer.MAX_VALUE);
    }

    static <B extends ByteBuf, K, V, M extends Map<K, V>> PortStreamCodec<B, M> map(
            final IntFunction<? extends M> factory, final PortStreamCodec<? super B, K> keyCodec, final PortStreamCodec<? super B, V> valueCodec, final int maxSize
    ) {
        return new PortStreamCodec<>() {
            public void encode(B buffer, M value) {
                writeCount(buffer, value.size(), maxSize);
                value.forEach((k, v) -> {
                    keyCodec.encode(buffer, k);
                    valueCodec.encode(buffer, v);
                });
            }

            public M decode(B buffer) {
                int i = readCount(buffer, maxSize);
                M m = factory.apply(Math.min(i, 65536));

                for (int j = 0; j < i; j++) {
                    K k = keyCodec.decode(buffer);
                    V v = valueCodec.decode(buffer);
                    m.put(k, v);
                }

                return m;
            }
        };
    }

    static <B extends FriendlyByteBuf> PortStreamCodec<B, ResourceKey<? extends Registry<?>>> registryKey() {
        return new PortStreamCodec<>() {
            @Override
            public ResourceKey<? extends Registry<?>> decode(B buf) {
                return ResourceKey.createRegistryKey(buf.readResourceLocation());
            }

            @Override
            public void encode(B buf, ResourceKey<? extends Registry<?>> value) {
                buf.writeResourceLocation(value.location());
            }
        };
    }

    private static <T, R> PortStreamCodec<PortRegistryFriendlyByteBuf, R> registry(ResourceKey<? extends Registry<T>> registryKey, Function<Registry<T>, IdMap<R>> idGetter) {
        return new PortStreamCodec<>() {
            private IdMap<R> getRegistryOrThrow(PortRegistryFriendlyByteBuf buffer) {
                var registry = buffer.registryAccess().registryOrThrow(registryKey);
                if (PortRegistryManager.isNonSyncedBuiltInRegistry(registry)) {
                    throw new IllegalStateException("Cannot use ID syncing for non-synced built-in registry: " + registry.key());
                }
                return idGetter.apply(registry);
            }

            public R decode(PortRegistryFriendlyByteBuf buffer) {
                int i = buffer.readVarInt();
                R value = getRegistryOrThrow(buffer).byId(i);
                if (value == null) throw new IllegalStateException("No value for id: " + i);
                return value;
            }

            public void encode(PortRegistryFriendlyByteBuf buffer, R value) {
                int i = PortIdMapExtension.getIdOrThrow(getRegistryOrThrow(buffer), value);
                buffer.writeVarInt(i);
            }
        };
    }

    static <T> PortStreamCodec<PortRegistryFriendlyByteBuf, T> registry(ResourceKey<? extends Registry<T>> registryKey) {
        return registry(registryKey, registry -> registry);
    }

    static <T> PortStreamCodec<PortRegistryFriendlyByteBuf, Holder<T>> holderRegistry(ResourceKey<? extends Registry<T>> registryKey) {
        return registry(registryKey, Registry::asHolderIdMap);
    }

    static <B extends ByteBuf, V> PortStreamCodec.PortCodecOperation<B, V, List<V>> list() {
        return codec -> collection(ArrayList::new, codec);
    }

    static <B extends ByteBuf, V, C extends Collection<V>> PortStreamCodec<B, C> collection(IntFunction<C> factory, PortStreamCodec<? super B, V> codec) {
        return collection(factory, codec, Integer.MAX_VALUE);
    }

    static <B extends ByteBuf, V, C extends Collection<V>> PortStreamCodec.PortCodecOperation<B, V, C> collection(IntFunction<C> factory) {
        return codec -> collection(factory, codec);
    }

    static int readCount(ByteBuf buffer, int maxSize) {
        int i = PortVarInt.read(buffer);
        if (i > maxSize) {
            throw new DecoderException(i + " elements exceeded max size of: " + maxSize);
        }
        return i;
    }

    static void writeCount(ByteBuf buffer, int count, int maxSize) {
        if (count > maxSize) {
            throw new EncoderException(count + " elements exceeded max size of: " + maxSize);
        }
        PortVarInt.write(buffer, count);
    }

    static <B extends ByteBuf, V, C extends Collection<V>> PortStreamCodec<B, C> collection(
            final IntFunction<C> factory, final PortStreamCodec<? super B, V> codec, final int maxSize
    ) {
        return new PortStreamCodec<>() {
            public C decode(B buffer) {
                int i = readCount(buffer, maxSize);
                C c = factory.apply(Math.min(i, 65536));

                for (int j = 0; j < i; j++) {
                    c.add(codec.decode(buffer));
                }

                return c;
            }

            public void encode(B buffer, C value) {
                writeCount(buffer, value.size(), maxSize);

                for (V v : value) {
                    codec.encode(buffer, v);
                }
            }
        };
    }

    static <T> PortStreamCodec<PortRegistryFriendlyByteBuf, T> fromCodecWithRegistriesTrusted(Codec<T> codec) {
        return fromCodecWithRegistries(codec, () -> new NbtAccounter(Long.MAX_VALUE));
    }

    static <T> PortStreamCodec<PortRegistryFriendlyByteBuf, T> fromCodecWithRegistries(Codec<T> codec) {
        return fromCodecWithRegistries(codec, () -> new NbtAccounter(2097152L));
    }

    static <T> PortStreamCodec<PortRegistryFriendlyByteBuf, T> fromCodecWithRegistries(Codec<T> codec, Supplier<NbtAccounter> accounterSupplier) {
        PortStreamCodec<ByteBuf, Tag> streamcodec = tagCodec(accounterSupplier);
        return new PortStreamCodec<>() {
            public T decode(PortRegistryFriendlyByteBuf buffer) {
                Tag tag = streamcodec.decode(buffer);
                RegistryOps<Tag> registryops = PortHolderLookupExtension.Provider.createSerializationContext(buffer.registryAccess(), NbtOps.INSTANCE);
                return PortDataResultExtension.getOrThrow(codec.parse(registryops, tag), message -> new DecoderException("Failed to decode: " + message + " " + tag));
            }

            public void encode(PortRegistryFriendlyByteBuf buffer, T value) {
                RegistryOps<Tag> registryops = PortHolderLookupExtension.Provider.createSerializationContext(buffer.registryAccess(), NbtOps.INSTANCE);
                Tag tag = PortDataResultExtension.getOrThrow(codec.encodeStart(registryops, value), message -> new EncoderException("Failed to encode: " + message + " " + value));
                streamcodec.encode(buffer, tag);
            }
        };
    }

    static PortStreamCodec<ByteBuf, String> stringUtf8(int maxLength) {
        return new PortStreamCodec<>() {
            public String decode(ByteBuf buffer) {
                return PortUtf8String.read(buffer, maxLength);
            }

            public void encode(ByteBuf buffer, String value) {
                PortUtf8String.write(buffer, value, maxLength);
            }
        };
    }

    static PortStreamCodec<ByteBuf, Tag> tagCodec(Supplier<NbtAccounter> accounter) {
        return new PortStreamCodec<>() {
            public Tag decode(ByteBuf buffer) {
                Tag tag = PortFriendlyByteBuf.readNbt(buffer, accounter.get());
                if (tag == null) {
                    throw new DecoderException("Expected non-null compound tag");
                }
                return tag;
            }

            public void encode(ByteBuf buffer, Tag value) {
                if (value == EndTag.INSTANCE) {
                    throw new EncoderException("Expected non-null compound tag");
                }
                PortFriendlyByteBuf.writeNbt(buffer, value);
            }
        };
    }

    static PortStreamCodec<ByteBuf, CompoundTag> compoundTagCodec(Supplier<NbtAccounter> accounterSupplier) {
        return tagCodec(accounterSupplier).map(tag -> {
            if (tag instanceof CompoundTag compoundTag) {
                return compoundTag;
            }
            throw new DecoderException("Not a compound tag: " + tag);
        }, Function.identity());
    }

    static <B extends ByteBuf, L, R> PortStreamCodec<B, Either<L, R>> either(PortStreamCodec<? super B, L> leftCodec, PortStreamCodec<? super B, R> rightCodec) {
        return new PortStreamCodec<>() {
            public Either<L, R> decode(B buffer) {
                return buffer.readBoolean()
                        ? Either.left(leftCodec.decode(buffer))
                        : Either.right(rightCodec.decode(buffer));
            }

            public void encode(B buffer, Either<L, R> either) {
                either.ifLeft(left -> {
                    buffer.writeBoolean(true);
                    leftCodec.encode(buffer, left);
                }).ifRight(right -> {
                    buffer.writeBoolean(false);
                    rightCodec.encode(buffer, right);
                });
            }
        };
    }

    static <T> PortStreamCodec<PortRegistryFriendlyByteBuf, HolderSet<T>> holderSet(ResourceKey<? extends Registry<T>> registryKey) {
        return new PortStreamCodec<>() {
            private static final int NAMED_SET = -1;
            private final PortStreamCodec<PortRegistryFriendlyByteBuf, Holder<T>> holderCodec = PortByteBufCodecs.holderRegistry(registryKey);
            private final Map<HolderSetType, PortStreamCodec<PortRegistryFriendlyByteBuf, ? extends ICustomHolderSet<T>>> holderSetCodecs = new ConcurrentHashMap<>();

            private PortStreamCodec<PortRegistryFriendlyByteBuf, ? extends ICustomHolderSet<T>> holderSetCodec(HolderSetType type) {
                return holderSetCodecs.computeIfAbsent(type, key -> {
                    Codec<? extends ICustomHolderSet<T>> codec = key.makeCodec(registryKey, RegistryFixedCodec.create(registryKey), false);
                    return new PortStreamCodec<>() {
                        @Override
                        public ICustomHolderSet<T> decode(PortRegistryFriendlyByteBuf buffer) {
                            return buffer.readJsonWithCodec(codec);
                        }

                        @Override
                        public void encode(PortRegistryFriendlyByteBuf buffer, ICustomHolderSet<T> value) {
                            buffer.writeJsonWithCodec(codec, cast(value));
                        }
                    };
                });
            }

            private <H extends ICustomHolderSet<T>> H cast(ICustomHolderSet<T> holderSet) {
                return (H) holderSet;
            }

            @Override
            public HolderSet<T> decode(PortRegistryFriendlyByteBuf buffer) {
                int i = PortVarInt.read(buffer) - 1;
                if (i < -1) {
                    return holderSetCodec(PortForgeRegistryExtension.byIdOrThrow((ForgeRegistry<HolderSetType>) ForgeRegistries.HOLDER_SET_TYPES.get(), -2 - i)).decode(buffer);
                }
                if (i == -1) {
                    Registry<T> registry = buffer.registryAccess().registryOrThrow(registryKey);
                    return registry.getTag(TagKey.create(registryKey, PortResourceLocationExtension.streamCodec().decode(buffer))).orElseThrow();
                }
                List<Holder<T>> list = new ArrayList<>(Math.min(i, 65536));

                for (int j = 0; j < i; j++) {
                    list.add(holderCodec.decode(buffer));
                }

                return HolderSet.direct(list);
            }

            @Override
            public void encode(PortRegistryFriendlyByteBuf buffer, HolderSet<T> value) {
                if (buffer.getConnectionType().isModded() && value instanceof ICustomHolderSet<T> customHolderSet) {
                    PortVarInt.write(buffer, -1 - ((ForgeRegistry<HolderSetType>) ForgeRegistries.HOLDER_SET_TYPES.get()).getID(customHolderSet.type()));
                    holderSetCodec(customHolderSet.type()).encode(buffer, cast(customHolderSet));
                    return;
                }
                Optional<TagKey<T>> optional = value.unwrapKey();
                if (optional.isPresent()) {
                    PortVarInt.write(buffer, 0);
                    PortResourceLocationExtension.streamCodec().encode(buffer, optional.get().location());
                    return;
                }
                PortVarInt.write(buffer, value.size() + 1);
                for (Holder<T> holder : value) {
                    holderCodec.encode(buffer, holder);
                }
            }
        };
    }

    static <B extends ByteBuf, V> PortStreamCodec<B, Optional<V>> optional(PortStreamCodec<B, V> codec) {
        return new PortStreamCodec<>() {
            @Override
            public Optional<V> decode(B buffer) {
                return buffer.readBoolean() ? Optional.of(codec.decode(buffer)) : Optional.empty();
            }

            @Override
            public void encode(B buffer, Optional<V> value) {
                if (value.isPresent()) {
                    buffer.writeBoolean(true);
                    codec.encode(buffer, value.get());
                } else {
                    buffer.writeBoolean(false);
                }
            }
        };
    }

    static <V> PortStreamCodec<ByteBuf, V> idMapper(IntFunction<V> idLookup, ToIntFunction<V> idGetter) {
        return new PortStreamCodec<>() {
            public V decode(ByteBuf buffer) {
                int i = PortVarInt.read(buffer);
                return idLookup.apply(i);
            }

            public void encode(ByteBuf buffer, V value) {
                int i = idGetter.applyAsInt(value);
                PortVarInt.write(buffer, i);
            }
        };
    }

    static <V> PortStreamCodec<FriendlyByteBuf, V> json(V defaultValue, Function<V, ? extends JsonElement> encoder, Function<? super JsonElement, V> decoder) {
        return new PortStreamCodec<>() {
            @Override
            public V decode(FriendlyByteBuf buffer) {
                CompoundTag nbt = buffer.readAnySizeNbt();
                if (nbt == null) {
                    return defaultValue;
                }
                if (buffer.readBoolean()) {
                    Tag tag = nbt.get("value");
                    if (tag == null) {
                        return defaultValue;
                    }
                    return decoder.apply(NbtOps.INSTANCE.convertTo(JsonOps.INSTANCE, tag));
                }
                return decoder.apply(NbtOps.INSTANCE.convertTo(JsonOps.INSTANCE, nbt));
            }

            @Override
            public void encode(FriendlyByteBuf buffer, V value) {
                JsonElement element = encoder.apply(value);
                Tag tag = JsonOps.INSTANCE.convertTo(NbtOps.INSTANCE, element);
                if (element.isJsonObject()) {
                    buffer.writeNbt((CompoundTag) tag);
                    buffer.writeBoolean(false);
                } else {
                    CompoundTag nbt = new CompoundTag();
                    nbt.put("value", tag);
                    buffer.writeNbt(nbt);
                    buffer.writeBoolean(true);
                }
            }
        };
    }
}
