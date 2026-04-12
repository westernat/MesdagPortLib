package org.mesdag.portlib.event.level;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.event.level.ChunkDataEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.wrapper.world.level.chunk.status.PortChunkType;

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

        public PortChunkType getType() {
            return PortChunkType.wrap(e.getStatus());
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
