package org.mesdag.portlib.event.level;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.neoforge.event.level.ChunkWatchEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public abstract class PortChunkWatchEvent<E extends ChunkWatchEvent> extends PortEvent<E> {
    @Diff
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
        @Diff
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

    public static class PortSent extends PortChunkWatchEvent<ChunkWatchEvent.Sent> {
        @Diff
        public PortSent(ChunkWatchEvent.Sent e) {
            super(e);
        }

        public LevelChunk getChunk() {
            return e.getChunk();
        }

        static {
            PortEventHooks.register();
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
