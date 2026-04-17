package PortLib.extensions.net.minecraft.server.level.ChunkMap;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import java.util.List;

@Extension
public class PortChunkMapExtension {
    public static List<ServerPlayer> getPlayersWatching(@This ChunkMap chunkMap, Entity entity) {
        return chunkMap.getPlayersWatching(entity);
    }
}