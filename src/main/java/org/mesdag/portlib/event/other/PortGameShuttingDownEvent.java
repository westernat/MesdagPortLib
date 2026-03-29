package org.mesdag.portlib.event.other;

import net.neoforged.neoforge.event.GameShuttingDownEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;


public class PortGameShuttingDownEvent extends PortEvent<GameShuttingDownEvent> {
    @Diff
    public PortGameShuttingDownEvent(GameShuttingDownEvent e) {
        super(e);
    }

    static {
        PortEventHooks.register(GameShuttingDownEvent.class, PortGameShuttingDownEvent.class, PortGameShuttingDownEvent::new);
    }
}
