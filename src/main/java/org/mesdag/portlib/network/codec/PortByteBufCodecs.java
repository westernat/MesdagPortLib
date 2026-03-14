package org.mesdag.portlib.network.codec;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import net.minecraft.core.IdMap;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import org.mesdag.portlib.diff.PortRegistryManager;
import org.mesdag.portlib.wrapper.core.PortIdMap;
import org.mesdag.portlib.wrapper.network.PortRegistryFriendlyByteBuf;
import org.mesdag.portlib.wrapper.network.PortVarInt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntFunction;

public interface PortByteBufCodecs {
    PortStreamCodec<FriendlyByteBuf, byte[]> UNBOUNDED_BYTE_ARRAY = new PortStreamCodec<>() {
        @Override
        public byte[] decode(FriendlyByteBuf buf) {
            return buf.readByteArray();
        }

        @Override
        public void encode(FriendlyByteBuf buf, byte[] data) {
            buf.writeByteArray(data);
        }
    };
    PortStreamCodec<ByteBuf, Boolean> BOOL = new PortStreamCodec<>() {
        public Boolean decode(ByteBuf buffer) {
            return buffer.readBoolean();
        }

        public void encode(ByteBuf buffer, Boolean value) {
            buffer.writeBoolean(value);
        }
    };

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

    static int readCount(ByteBuf buffer, int maxSize) {
        int i = PortVarInt.read(buffer);
        if (i > maxSize) {
            throw new DecoderException(i + " elements exceeded max size of: " + maxSize);
        } else {
            return i;
        }
    }

    static void writeCount(ByteBuf buffer, int count, int maxSize) {
        if (count > maxSize) {
            throw new EncoderException(count + " elements exceeded max size of: " + maxSize);
        } else {
            PortVarInt.write(buffer, count);
        }
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
}
