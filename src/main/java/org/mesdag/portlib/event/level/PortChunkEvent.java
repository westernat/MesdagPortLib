package org.mesdag.portlib.event.level;

import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraftforge.event.level.ChunkEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;

public abstract class PortChunkEvent<E extends ChunkEvent> extends PortLevelEvent<E> {
    protected final E e;

    @Diff
    public PortChunkEvent(E e) {
        super(e);
        this.e = e;
    }

    public ChunkAccess getChunk() {
        return e.getChunk();
    }

    public static class Load extends PortChunkEvent<ChunkEvent.Load> {
        @Diff
        public Load(ChunkEvent.Load e) {
            super(e);
        }

        public boolean isNewChunk() {
            return e.isNewChunk();
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class Unload extends PortChunkEvent<ChunkEvent.Unload> {
        @Diff
        public Unload(ChunkEvent.Unload e) {
            super(e);
        }

        static {
            PortEventHooks.register();
        }
    }
}
