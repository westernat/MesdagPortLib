package org.mesdag.portlib.event.level;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.chunk.status.ChunkType;
import net.neoforged.neoforge.event.level.ChunkDataEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;

public abstract class PortChunkDataEvent<E extends ChunkDataEvent> extends PortChunkEvent<E> {
    @Diff
    public PortChunkDataEvent(E e) {
        super(e);
    }

    public CompoundTag getData() {
        return e.getData();
    }

    public static class PortLoad extends PortChunkDataEvent<ChunkDataEvent.Load> {
        @Diff
        public PortLoad(ChunkDataEvent.Load e) {
            super(e);
        }

        public ChunkType getType() {
            return e.getType();
        }

        static {
            PortEventHooks.register(ChunkDataEvent.Load.class, PortChunkDataEvent.PortLoad.class, PortChunkDataEvent.PortLoad::new);
        }
    }

    public static class PortSave extends PortChunkDataEvent<ChunkDataEvent.Save> {
        @Diff
        public PortSave(ChunkDataEvent.Save e) {
            super(e);
        }

        static {
            PortEventHooks.register(ChunkDataEvent.Save.class, PortSave.class, PortSave::new);
        }
    }
}
