package org.mesdag.portlib.diff;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.mesdag.portlib.PortLib;
import org.mesdag.portlib.network.IPortPacket;
import org.mesdag.portlib.network.PortRegistryFriendlyByteBuf;
import org.mesdag.portlib.network.codec.PortByteBufCodecs;
import org.mesdag.portlib.network.codec.PortStreamCodec;
import org.mesdag.portlib.wrapper.common.extensions.IPortParticleOptionsExtension;

import java.util.List;

public record PortSyncEffectParticlesS2C(
        int entityId,
        List<ParticleOptions> list
) implements IPortPacket.S2C {
    private static final int MAX_EFFECT_PARTICLES = 256;
    public static final ResourceLocation IDENTIFIER = PortLib.asResource("sync_effect_particles");
    public static final PortStreamCodec<PortRegistryFriendlyByteBuf, PortSyncEffectParticlesS2C> STREAM_CODEC = PortStreamCodec.composite(
            PortByteBufCodecs.VAR_INT, PortSyncEffectParticlesS2C::entityId,
            IPortParticleOptionsExtension.STREAM_CODEC.apply(PortByteBufCodecs.list(MAX_EFFECT_PARTICLES)), PortSyncEffectParticlesS2C::list,
            PortSyncEffectParticlesS2C::new
    );

    @Override
    public void work(Player player) {
        if (player.level().getEntity(entityId) instanceof IPortLivingEntity living) {
            living.portlib$setEffectParticles(list);
        }
    }

    @Override
    public ResourceLocation identifier() {
        return IDENTIFIER;
    }
}
