package PortLib.extensions.net.minecraft.core.particles.ParticleTypes;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import org.mesdag.portlib.network.codec.PortStreamCodec;

public class PortParticleTypesExtension {
    private static final PortStreamCodec<FriendlyByteBuf, ParticleOptions> STREAM_CODEC = new PortStreamCodec<>() {
        @Override
        public ParticleOptions decode(FriendlyByteBuf buffer) {
            ResourceLocation id = buffer.readResourceLocation();
            ParticleType type = ForgeRegistries.PARTICLE_TYPES.getValue(id);
            return type.getDeserializer().fromNetwork(type, buffer);
        }

        @Override
        public void encode(FriendlyByteBuf buffer, ParticleOptions value) {

        }
    };

    public static PortStreamCodec<FriendlyByteBuf, ParticleOptions> streamCodec() {
        return STREAM_CODEC;
    }
}
