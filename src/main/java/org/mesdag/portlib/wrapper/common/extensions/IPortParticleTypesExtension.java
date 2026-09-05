package org.mesdag.portlib.wrapper.common.extensions;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import org.mesdag.portlib.network.codec.PortStreamCodec;

public interface IPortParticleTypesExtension {
    PortStreamCodec<FriendlyByteBuf, ParticleOptions> STREAM_CODEC = new PortStreamCodec<>() {
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

}
