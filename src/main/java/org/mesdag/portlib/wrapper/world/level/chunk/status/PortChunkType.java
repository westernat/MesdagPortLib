package org.mesdag.portlib.wrapper.world.level.chunk.status;

import net.minecraft.world.level.chunk.status.ChunkType;
import org.mesdag.portlib.diff.Diff;

public enum PortChunkType {
    PROTOCHUNK,
    LEVELCHUNK;

    @Diff
    public ChunkType unwrap() {
        return this == PROTOCHUNK ? ChunkType.PROTOCHUNK : ChunkType.LEVELCHUNK;
    }

    @Diff
    public static PortChunkType wrap(ChunkType chunkType) {
        return chunkType == ChunkType.PROTOCHUNK ? PROTOCHUNK : LEVELCHUNK;
    }
}
