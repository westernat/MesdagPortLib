package PortLib.extensions.net.minecraft.core.particles.ParticleOptions;

import com.mojang.brigadier.StringReader;
import manifold.ext.rt.api.Extension;
import net.minecraft.commands.arguments.ParticleArgument;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import org.mesdag.portlib.network.PortRegistryFriendlyByteBuf;
import org.mesdag.portlib.network.codec.PortStreamCodec;

@Extension
public class PortParticleOptionsExtension {
    private static final PortStreamCodec<PortRegistryFriendlyByteBuf, ParticleOptions> STREAM_CODEC = new PortStreamCodec<>() {
        @Override
        public void encode(PortRegistryFriendlyByteBuf buffer, ParticleOptions value) {
            buffer.writeUtf(value.writeToString());
        }

        @Override
        public ParticleOptions decode(PortRegistryFriendlyByteBuf buffer) {
            HolderLookup.RegistryLookup<ParticleType<?>> lookup = buffer.registryAccess().lookupOrThrow(Registries.PARTICLE_TYPE);
            try {
                return ParticleArgument.readParticle(new StringReader(buffer.readUtf()), lookup);
            } catch (Exception e) {
                throw new IllegalStateException("Unable to read particle options");
            }
        }
    };

    @Extension
    public static PortStreamCodec<PortRegistryFriendlyByteBuf, ParticleOptions> streamCodec() {
        return STREAM_CODEC;
    }
}
