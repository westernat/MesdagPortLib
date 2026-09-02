package org.mesdag.portlib.wrapper.common.extensions;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import org.mesdag.portlib.diff.IPortServerPlayer;

public interface IPortServerPlayerExtension extends IPortPlayerExtension {
    private ServerPlayer self() {
        return (ServerPlayer) this;
    }

    default int requestedViewDistance() {
        return self().serverLevel().getChunkSource().chunkMap.getDistanceManager().playerTicketManager.viewDistance;
    }

    @Override
    default Vec3 getKnownMovement() {
        return IPortServerPlayer.of(self()).portlib$getKnownMovement();
    }
}
