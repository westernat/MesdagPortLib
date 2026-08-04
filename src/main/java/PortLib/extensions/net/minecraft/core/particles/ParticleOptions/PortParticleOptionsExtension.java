package PortLib.extensions.net.minecraft.core.particles.ParticleOptions;

import com.mojang.brigadier.StringReader;
import net.minecraft.commands.arguments.ParticleArgument;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import org.mesdag.portlib.network.PortRegistryFriendlyByteBuf;
import org.mesdag.portlib.network.codec.PortStreamCodec;

public class PortParticleOptionsExtension {
    private static final int MAX_SERIALIZED_LENGTH = 32767;
    private static final PortStreamCodec<PortRegistryFriendlyByteBuf, ParticleOptions> STREAM_CODEC = new PortStreamCodec<>() {
        @Override
        public void encode(PortRegistryFriendlyByteBuf buffer, ParticleOptions value) {
            buffer.writeUtf(value.writeToString(), MAX_SERIALIZED_LENGTH);
        }

        @Override
        public ParticleOptions decode(PortRegistryFriendlyByteBuf buffer) {
            HolderLookup.RegistryLookup<ParticleType<?>> lookup = buffer.registryAccess()
                    .lookup(Registries.PARTICLE_TYPE)
                    .orElseGet(BuiltInRegistries.PARTICLE_TYPE::asLookup);
            try {
                return ParticleArgument.readParticle(new StringReader(buffer.readUtf(MAX_SERIALIZED_LENGTH)), lookup);
            } catch (Exception exception) {
                throw new IllegalStateException("Unable to read particle options", exception);
            }
        }
    };

    public static PortStreamCodec<PortRegistryFriendlyByteBuf, ParticleOptions> streamCodec() {
        return STREAM_CODEC;
    }
}
