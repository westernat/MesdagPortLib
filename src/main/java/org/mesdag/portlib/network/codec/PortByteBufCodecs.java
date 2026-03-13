package org.mesdag.portlib.network.codec;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import net.minecraft.core.IdMap;
import net.minecraft.core.Registry;
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

    static <B extends ByteBuf, V> PortStreamCodec.CodecOperation<B, V, List<V>> list() {
        return p_320272_ -> collection(ArrayList::new, p_320272_);
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
        return new PortStreamCodec<B, C>() {
            public C decode(B p_324220_) {
                int i = readCount(p_324220_, maxSize);
                C c = factory.apply(Math.min(i, 65536));

                for (int j = 0; j < i; j++) {
                    c.add(codec.decode(p_324220_));
                }

                return c;
            }

            public void encode(B p_323874_, C p_340813_) {
                writeCount(p_323874_, p_340813_.size(), maxSize);

                for (V v : p_340813_) {
                    codec.encode(p_323874_, v);
                }
            }
        };
    }
}
