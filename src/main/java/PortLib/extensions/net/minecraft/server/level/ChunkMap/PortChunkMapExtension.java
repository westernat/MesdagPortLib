package PortLib.extensions.net.minecraft.server.level.ChunkMap;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Extension
public class PortChunkMapExtension {
    public static List<ServerPlayer> getPlayersWatching(@This ChunkMap chunkMap, Entity entity) {
        var trackedEntity = chunkMap.entityMap.get(entity.getId());
        if (trackedEntity != null) {
            var ret = new ArrayList<ServerPlayer>(trackedEntity.seenBy.size());
            for (var connection : trackedEntity.seenBy) {
                ret.add(connection.getPlayer());
            }
            return Collections.unmodifiableList(ret);
        } else {
            return List.of();
        }
    }
}
