package org.mesdag.portlib.event.level;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.neoforge.event.level.ChunkWatchEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public abstract class PortChunkWatchEvent extends PortEvent {
    private final ChunkWatchEvent e;

    @Diff
    public PortChunkWatchEvent(ChunkWatchEvent e) {
        this.e = e;
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

    public abstract static class PortWatch extends PortChunkWatchEvent {
        @Diff
        public PortWatch(ChunkWatchEvent e) {
            super(e);
        }

        public abstract LevelChunk getChunk();

        static {
            PortEventHooks.register(ChunkWatchEvent.Watch.class, PortWatch.class, e -> new PortWatch(e) {
                @Override
                public LevelChunk getChunk() {
                    return e.getChunk();
                }
            });
        }
    }

    public abstract static class PortSent extends PortChunkWatchEvent {
        @Diff
        public PortSent(ChunkWatchEvent e) {
            super(e);
        }

        public abstract LevelChunk getChunk();

        static {
            PortEventHooks.register(ChunkWatchEvent.Sent.class, PortSent.class, e -> new PortSent(e) {
                @Override
                public LevelChunk getChunk() {
                    return e.getChunk();
                }
            });
        }
    }

    public static class PortUnWatch extends PortChunkWatchEvent {
        @Diff
        public PortUnWatch(ChunkWatchEvent e) {
            super(e);
        }

        static {
            PortEventHooks.register(ChunkWatchEvent.UnWatch.class, PortUnWatch.class, PortUnWatch::new);
        }
    }
}
