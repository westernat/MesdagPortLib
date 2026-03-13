package org.mesdag.portlib.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.event.level.ChunkWatchEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.level.PortChunkWatchEvent;

public class PortEventHooks {
    public static void init() {
        PortEventHandler.wrapEvent(false, ChunkWatchEvent.Watch.class, PortChunkWatchEvent.Watch::new);
        PortEventHandler.wrapEvent(false, ChunkWatchEvent.UnWatch.class, PortChunkWatchEvent.UnWatch::new);
    }

    @Diff
    public static void fireChunkSent(ServerPlayer entity, LevelChunk chunk, ServerLevel level) {
        PortEventHandler.postEvent(new PortChunkWatchEvent.Sent(entity, chunk, level));
    }
}
