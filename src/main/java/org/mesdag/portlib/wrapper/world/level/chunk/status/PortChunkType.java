package org.mesdag.portlib.wrapper.world.level.chunk.status;

import net.minecraft.world.level.chunk.ChunkStatus;
import org.mesdag.portlib.diff.Diff;

public enum PortChunkType {
    PROTOCHUNK,
    LEVELCHUNK;

    @Diff
    public ChunkStatus.ChunkType unwrap() {
        return this == PROTOCHUNK ? ChunkStatus.ChunkType.PROTOCHUNK : ChunkStatus.ChunkType.LEVELCHUNK;
    }

    @Diff
    public static PortChunkType wrap(ChunkStatus.ChunkType chunkType) {
        return chunkType == ChunkStatus.ChunkType.PROTOCHUNK ? PROTOCHUNK : LEVELCHUNK;
    }
}
