package org.mesdag.portlib.event;

import net.neoforged.neoforge.event.GameShuttingDownEvent;
import org.mesdag.portlib.diff.Diff;


public class PortGameShuttingDownEvent extends PortEvent {
    private final GameShuttingDownEvent e;

    @Diff
    public PortGameShuttingDownEvent(GameShuttingDownEvent e) {
        this.e = e;
    }

    static {
        PortEventHooks.register(GameShuttingDownEvent.class, PortGameShuttingDownEvent.class, PortGameShuttingDownEvent::new);
    }
}