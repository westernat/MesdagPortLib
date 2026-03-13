package org.mesdag.portlib.event.level;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.event.level.ChunkWatchEvent;

public abstract class PortChunkWatchEvent extends ChunkWatchEvent {
    public PortChunkWatchEvent(ServerPlayer player, ChunkPos pos, ServerLevel level) {
        super(player, pos, level);
    }

    public static class Watch extends ChunkWatchEvent.Watch {
        public Watch(Watch event) {
            super(event.getPlayer(), event.getChunk(), event.getLevel());
        }
    }

    public static class Sent extends ChunkWatchEvent {
        private final LevelChunk chunk;

        public Sent(ServerPlayer player, LevelChunk chunk, ServerLevel level) {
            super(player, chunk.getPos(), level);
            this.chunk = chunk;
        }

        public LevelChunk getChunk() {
            return this.chunk;
        }
    }

    public static class UnWatch extends ChunkWatchEvent.UnWatch {
        public UnWatch(UnWatch event) {
            super(event.getPlayer(), event.getPos(), event.getLevel());
        }
    }
}
