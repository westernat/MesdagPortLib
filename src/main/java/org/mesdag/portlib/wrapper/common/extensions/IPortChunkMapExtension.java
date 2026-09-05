package org.mesdag.portlib.wrapper.common.extensions;

import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface IPortChunkMapExtension {
    private ChunkMap self() {
        return (ChunkMap) this;
    }

    default List<ServerPlayer> getPlayersWatching(Entity entity) {
        var trackedEntity = self().entityMap.get(entity.getId());
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

    static IPortChunkMapExtension of(ChunkMap chunkMap) {
        return (IPortChunkMapExtension) chunkMap;
    }
}
