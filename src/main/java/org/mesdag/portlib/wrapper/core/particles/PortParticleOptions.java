package org.mesdag.portlib.wrapper.core.particles;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import org.mesdag.portlib.network.PortRegistryFriendlyByteBuf;
import org.mesdag.portlib.network.codec.PortStreamCodec;

public class PortParticleOptions implements ParticleOptions {
    private final ParticleType<PortParticleOptions> type;

    @SuppressWarnings("unchecked")
    public <T extends PortParticleOptions> PortParticleOptions(ParticleType<T> type, MapCodec<T> codec, PortStreamCodec<? super PortRegistryFriendlyByteBuf, T> streamCodec) {
        this.type = (ParticleType<PortParticleOptions>) type;
    }

    @Override
    public ParticleType<? extends PortParticleOptions> getType() {
        return type;
    }
}
