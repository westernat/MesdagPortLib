package org.mesdag.portlib.network.codec;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import org.mesdag.portlib.network.PortRegistryFriendlyByteBuf;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.IntFunction;
import java.util.function.Supplier;

@SuppressWarnings({"unchecked", "rawtypes"})
public interface PortByteBufCodecs {
    PortStreamCodec<ByteBuf, Boolean> BOOL = PortStreamCodec.wrap(ByteBufCodecs.BOOL);
    PortStreamCodec<ByteBuf, Byte> BYTE = PortStreamCodec.wrap(ByteBufCodecs.BYTE);
    PortStreamCodec<ByteBuf, Short> SHORT = PortStreamCodec.wrap(ByteBufCodecs.SHORT);
    PortStreamCodec<ByteBuf, Integer> UNSIGNED_SHORT = PortStreamCodec.wrap(ByteBufCodecs.UNSIGNED_SHORT);
    PortStreamCodec<ByteBuf, Integer> INT = PortStreamCodec.wrap(ByteBufCodecs.INT);
    PortStreamCodec<ByteBuf, Integer> VAR_INT = PortStreamCodec.wrap(ByteBufCodecs.VAR_INT);
    PortStreamCodec<ByteBuf, Long> VAR_LONG = PortStreamCodec.wrap(ByteBufCodecs.VAR_LONG);
    PortStreamCodec<ByteBuf, Float> FLOAT = PortStreamCodec.wrap(ByteBufCodecs.FLOAT);
    PortStreamCodec<ByteBuf, Double> DOUBLE = PortStreamCodec.wrap(ByteBufCodecs.DOUBLE);
    PortStreamCodec<ByteBuf, byte[]> BYTE_ARRAY = PortStreamCodec.wrap(ByteBufCodecs.BYTE_ARRAY);
    PortStreamCodec<FriendlyByteBuf, byte[]> UNBOUNDED_BYTE_ARRAY = PortStreamCodec.wrap(NeoForgeStreamCodecs.UNBOUNDED_BYTE_ARRAY);
    PortStreamCodec<ByteBuf, String> STRING_UTF8 = PortStreamCodec.wrap(ByteBufCodecs.STRING_UTF8);
    PortStreamCodec<ByteBuf, Tag> TAG = PortStreamCodec.wrap(ByteBufCodecs.TAG);
    PortStreamCodec<ByteBuf, Tag> TRUSTED_TAG = PortStreamCodec.wrap(ByteBufCodecs.TRUSTED_TAG);
    PortStreamCodec<ByteBuf, CompoundTag> COMPOUND_TAG = PortStreamCodec.wrap(ByteBufCodecs.COMPOUND_TAG);
    PortStreamCodec<ByteBuf, CompoundTag> TRUSTED_COMPOUND_TAG = PortStreamCodec.wrap(ByteBufCodecs.TRUSTED_COMPOUND_TAG);
    PortStreamCodec<ByteBuf, Optional<CompoundTag>> OPTIONAL_COMPOUND_TAG = PortStreamCodec.wrap(ByteBufCodecs.OPTIONAL_COMPOUND_TAG);
    PortStreamCodec<ByteBuf, ResourceLocation> RESOURCE_LOCATION = PortStreamCodec.wrap(ResourceLocation.STREAM_CODEC);

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

    static <T> PortStreamCodec<PortRegistryFriendlyByteBuf, T> fromCodecWithRegistries(Codec<T> codec) {
        return (PortStreamCodec) PortStreamCodec.wrap(ByteBufCodecs.fromCodecWithRegistries(codec));
    }

    static <T> PortStreamCodec<PortRegistryFriendlyByteBuf, T> fromCodecWithRegistries(Codec<T> codec, Supplier<NbtAccounter> accounterSupplier) {
        return (PortStreamCodec) PortStreamCodec.wrap(ByteBufCodecs.fromCodecWithRegistries(codec, accounterSupplier));
    }

    static PortStreamCodec<ByteBuf, Tag> tagCodec(Supplier<NbtAccounter> accounter) {
        return PortStreamCodec.wrap(ByteBufCodecs.tagCodec(accounter));
    }
}
