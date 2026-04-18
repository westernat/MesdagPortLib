package org.mesdag.portlib.event.level;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.event.level.ChunkWatchEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public abstract class PortChunkWatchEvent<E extends ChunkWatchEvent> extends PortEvent<E> {
    public PortChunkWatchEvent(E e) {
        super(e);
    }

    public ServerPlayer getPlayer() {
        return e.getPlayer();
    }

    public ChunkPos getPos() {
        return e.getPos();
    }

    public ServerLevel getLevel() {
        return e.getLevel();
    }

    public static class PortWatch extends PortChunkWatchEvent<ChunkWatchEvent.Watch> {
        public PortWatch(ChunkWatchEvent.Watch e) {
            super(e);
        }

        public LevelChunk getChunk() {
            return e.getChunk();
        }

        static {
            PortEventHooks.register();
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

    public static class PortUnWatch extends PortChunkWatchEvent<ChunkWatchEvent.UnWatch> {
        @Diff
        public PortUnWatch(ChunkWatchEvent.UnWatch e) {
            super(e);
        }

        static {
            PortEventHooks.register();
        }
    }
}
