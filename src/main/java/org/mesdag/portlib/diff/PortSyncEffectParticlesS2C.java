package org.mesdag.portlib.diff;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.player.Player;
import org.mesdag.portlib.PortLib;
import org.mesdag.portlib.network.IPortPacket;
import org.mesdag.portlib.network.PortRegistryFriendlyByteBuf;
import org.mesdag.portlib.network.codec.PortByteBufCodecs;
import org.mesdag.portlib.network.codec.PortStreamCodec;
import org.mesdag.portlib.wrapper.resources.PortIdentifier;

import java.util.List;

public record PortSyncEffectParticlesS2C(int entityId, List<ParticleOptions> list) implements IPortPacket.S2C {
    public static final PortIdentifier IDENTIFIER = PortLib.asResource("sync_effect_particles");
    public static final PortStreamCodec<PortRegistryFriendlyByteBuf, PortSyncEffectParticlesS2C> STREAM_CODEC = PortStreamCodec.composite(
            PortByteBufCodecs.VAR_INT, PortSyncEffectParticlesS2C::entityId,
            ParticleOptions.streamCodec().apply(PortByteBufCodecs.list()), PortSyncEffectParticlesS2C::list,
            PortSyncEffectParticlesS2C::new
    );

    @Override
    public void work(Player player) {
        if (player.level().getEntity(entityId) instanceof IPortLivingEntity living) {
            living.portlib$setEffectParticles(list);
        }
    }

    @Override
    public PortIdentifier identifier() {
        return IDENTIFIER;
    }
}
