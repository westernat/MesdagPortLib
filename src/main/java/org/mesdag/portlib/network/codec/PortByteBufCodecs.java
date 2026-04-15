package org.mesdag.portlib.network.codec;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import net.minecraft.core.IdMap;
import net.minecraft.core.Registry;
import net.minecraft.nbt.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.mesdag.portlib.diff.PortRegistryManager;
import org.mesdag.portlib.network.*;
import org.mesdag.portlib.wrapper.core.PortIdMap;
import org.mesdag.portlib.wrapper.resources.PortIdentifier;

import java.util.*;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;

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
    PortStreamCodec<ByteBuf, PortIdentifier> IDENTIFIER = STRING_UTF8.map(PortIdentifier::parse, PortIdentifier::toString);

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

    private static <T, R> PortStreamCodec<PortRegistryFriendlyByteBuf, R> registry(
            final ResourceKey<? extends Registry<T>> registryKey, final Function<Registry<T>, IdMap<R>> idGetter
    ) {
        return new PortStreamCodec<>() {
            private IdMap<R> getRegistryOrThrow(PortRegistryFriendlyByteBuf p_330361_) {
                var registry = p_330361_.registryAccess().registryOrThrow(registryKey);
                if (PortRegistryManager.isNonSyncedBuiltInRegistry(registry)) {
                    throw new IllegalStateException("Cannot use ID syncing for non-synced built-in registry: " + registry.key());
                }
                return idGetter.apply(registry);
            }

            public R decode(PortRegistryFriendlyByteBuf buffer) {
                int i = buffer.readVarInt();
                return getRegistryOrThrow(buffer).byIdOrThrow(i);
            }

            public void encode(PortRegistryFriendlyByteBuf buffer, R value) {
                int i = PortIdMap.getIdOrThrow(getRegistryOrThrow(buffer), value);
                buffer.writeVarInt(i);
            }
        };
    }

    static <T> PortStreamCodec<PortRegistryFriendlyByteBuf, T> registry(ResourceKey<? extends Registry<T>> registryKey) {
        return registry(registryKey, registry -> registry);
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

    static <T> PortStreamCodec<PortRegistryFriendlyByteBuf, T> fromCodecWithRegistries(Codec<T> codec) {
        return fromCodecWithRegistries(codec, () -> new NbtAccounter(2097152L));
    }

    static <T> PortStreamCodec<PortRegistryFriendlyByteBuf, T> fromCodecWithRegistries(Codec<T> codec, Supplier<NbtAccounter> accounterSupplier) {
        PortStreamCodec<ByteBuf, Tag> streamcodec = tagCodec(accounterSupplier);
        return new PortStreamCodec<>() {
            public T decode(PortRegistryFriendlyByteBuf buffer) {
                Tag tag = streamcodec.decode(buffer);
                RegistryOps<Tag> registryops = buffer.registryAccess().createSerializationContext(NbtOps.INSTANCE);
                return codec.parse(registryops, tag).getOrThrow(false, message -> {
                    throw new DecoderException("Failed to decode: " + message + " " + tag);
                });
            }

            public void encode(PortRegistryFriendlyByteBuf buffer, T value) {
                RegistryOps<Tag> registryops = buffer.registryAccess().createSerializationContext(NbtOps.INSTANCE);
                Tag tag = codec.encodeStart(registryops, value).getOrThrow(false, message -> {
                    throw new EncoderException("Failed to encode: " + message + " " + value);
                });
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
}
