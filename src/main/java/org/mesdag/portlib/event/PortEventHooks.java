package org.mesdag.portlib.event;

import net.neoforged.neoforge.event.level.ChunkWatchEvent;
import org.mesdag.portlib.event.level.PortChunkWatchEvent;

public class PortEventHooks {
    public static void init() {
        PortEventHandler.wrapEvent(false, ChunkWatchEvent.Sent.class, PortChunkWatchEvent.Sent::new);
    }
}
