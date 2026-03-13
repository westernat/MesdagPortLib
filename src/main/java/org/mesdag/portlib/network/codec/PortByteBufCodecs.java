package org.mesdag.portlib.network.codec;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import org.mesdag.portlib.wrapper.network.PortRegistryFriendlyByteBuf;

import java.util.Collection;
import java.util.List;
import java.util.function.IntFunction;

public interface PortByteBufCodecs {
    PortStreamCodec<FriendlyByteBuf, byte[]> UNBOUNDED_BYTE_ARRAY = PortStreamCodec.wrap(NeoForgeStreamCodecs.UNBOUNDED_BYTE_ARRAY);

    static <T> PortStreamCodec<PortRegistryFriendlyByteBuf, T> registry(ResourceKey<? extends Registry<T>> registryKey) {
        return (PortStreamCodec) PortStreamCodec.wrap(ByteBufCodecs.registry(registryKey));
    }

    static <B extends ByteBuf, V> PortStreamCodec.PortCodecOperation<B, V, List<V>> list() {
        return PortStreamCodec.PortCodecOperation.wrap(ByteBufCodecs.list());
    }

    static <B extends ByteBuf, V, C extends Collection<V>> PortStreamCodec<B, C> collection(IntFunction<C> factory, PortStreamCodec<? super B, V> codec) {
        return collection(factory, codec, Integer.MAX_VALUE);
    }

    static int readCount(ByteBuf buffer, int maxSize) {
        return ByteBufCodecs.readCount(buffer, maxSize);
    }

    static void writeCount(ByteBuf buffer, int count, int maxSize) {
        ByteBufCodecs.writeCount(buffer, count, maxSize);
    }

    static <B extends ByteBuf, V, C extends Collection<V>> PortStreamCodec<B, C> collection(IntFunction<C> factory, PortStreamCodec<? super B, V> codec, int maxSize) {
        return PortStreamCodec.wrap(ByteBufCodecs.collection(factory, codec.unwrap(), maxSize));
    }
}
