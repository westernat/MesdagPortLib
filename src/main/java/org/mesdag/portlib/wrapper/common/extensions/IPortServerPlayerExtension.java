package org.mesdag.portlib.wrapper.common.extensions;

import net.minecraft.server.level.ServerPlayer;

public interface IPortServerPlayerExtension extends IPortPlayerExtension {
    default int requestedViewDistance() {
        return ((ServerPlayer) this).serverLevel().getChunkSource().chunkMap.getDistanceManager().playerTicketManager.viewDistance;
    }
}
