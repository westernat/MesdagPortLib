package org.mesdag.portlib.event.level;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import org.mesdag.portlib.event.PortEvent;

public abstract class PortChunkWatchEvent extends PortEvent {
    public abstract ServerPlayer getPlayer();

    public abstract ChunkPos getPos();

    public abstract ServerLevel getLevel();

    public abstract static class PortWatch extends PortChunkWatchEvent {
        public abstract LevelChunk getChunk();
    }

    public abstract static class PortSent extends PortChunkWatchEvent {
        public abstract LevelChunk getChunk();
    }

    public abstract static class PortUnWatch extends PortChunkWatchEvent {}
}
