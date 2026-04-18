package org.mesdag.portlib.event.level;

import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.event.level.ChunkTicketLevelUpdatedEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

import javax.annotation.Nullable;

public class PortChunkTicketLevelUpdatedEvent extends PortEvent<ChunkTicketLevelUpdatedEvent> {
    @Diff
    public PortChunkTicketLevelUpdatedEvent(ChunkTicketLevelUpdatedEvent e) {
        super(e);
    }

    public ServerLevel getLevel() {
        return e.getLevel();
    }

    public long getChunkPos() {
        return e.getChunkPos();
    }

    public int getOldTicketLevel() {
        return e.getOldTicketLevel();
    }

    public int getNewTicketLevel() {
        return e.getNewTicketLevel();
    }

    public @Nullable ChunkHolder getChunkHolder() {
        return e.getChunkHolder();
    }

    static {
        PortEventHooks.register();
    }
}
