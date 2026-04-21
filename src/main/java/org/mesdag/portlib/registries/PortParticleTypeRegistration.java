package org.mesdag.portlib.registries;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.mesdag.portlib.network.PortRegistryFriendlyByteBuf;
import org.mesdag.portlib.network.codec.PortStreamCodec;
import org.mesdag.portlib.wrapper.core.particles.PortParticleOptions;

public class PortParticleTypeRegistration extends PortRegistration<ParticleType<?>> {
    PortParticleTypeRegistration(String namespace) {
        super(namespace, Registries.PARTICLE_TYPE);
    }

    public <T extends PortParticleOptions> PortRegistryEntry<ParticleType<?>, ParticleType<T>> registerTyped(String name, boolean overrideLimiter, MapCodec<T> codec, PortStreamCodec<? super PortRegistryFriendlyByteBuf, T> streamCodec) {
        return register(name, () -> new ParticleType<>(overrideLimiter) {
            @SuppressWarnings("unchecked")
            private final StreamCodec<? super RegistryFriendlyByteBuf, T> _streamCodec = (StreamCodec<? super RegistryFriendlyByteBuf, T>) streamCodec.unwrap();

            @Override
            public MapCodec<T> codec() {
                return codec;
            }

            @Override
            public StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() {
                return _streamCodec;
            }
        });
    }

    public PortRegistryEntry<ParticleType<?>, SimpleParticleType> register(String name, boolean overrideLimiter) {
        return register(name, () -> new SimpleParticleType(overrideLimiter));
    }
}
