package org.mesdag.portlib.wrapper.server.level;

import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import java.util.List;

public class PortChunkMap {
    public static List<ServerPlayer> getPlayersWatching(ChunkMap chunkMap, Entity entity) {
        return chunkMap.getPlayersWatching(entity);
    }
}
