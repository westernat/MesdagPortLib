package org.mesdag.portlib.wrapper.core.particles;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.ApiStatus;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.network.PortRegistryFriendlyByteBuf;
import org.mesdag.portlib.network.codec.PortStreamCodec;

public class PortParticleOptions implements ParticleOptions {
    private final ParticleType<PortParticleOptions> type;
    private final MapCodec<PortParticleOptions> codec;
    private final PortStreamCodec<? super PortRegistryFriendlyByteBuf, PortParticleOptions> streamCodec;

    @SuppressWarnings("unchecked")
    public <T extends PortParticleOptions> PortParticleOptions(ParticleType<T> type, MapCodec<T> codec, PortStreamCodec<? super PortRegistryFriendlyByteBuf, T> streamCodec) {
        this.type = (ParticleType<PortParticleOptions>) type;
        this.codec = (MapCodec<PortParticleOptions>) codec;
        this.streamCodec = (PortStreamCodec<? super PortRegistryFriendlyByteBuf, PortParticleOptions>) streamCodec;
    }

    @Override
    public ParticleType<? extends PortParticleOptions> getType() {
        return type;
    }

    @Diff
    @ApiStatus.NonExtendable
    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
        streamCodec.encode(PortRegistryFriendlyByteBuf.wrap(buffer), this);
    }

    @Diff
    @ApiStatus.NonExtendable
    @Override
    public String writeToString() {
        NbtOps ops = NbtOps.INSTANCE;
        return codec.encode(this, ops, codec.compressedBuilder(ops)).build(ops.empty())
                .getOrThrow(false, message -> {
                    throw new RuntimeException(message);
                }).getAsString();
    }
}
