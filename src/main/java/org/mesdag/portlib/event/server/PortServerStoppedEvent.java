package org.mesdag.portlib.event.server;

import net.minecraftforge.event.server.ServerStoppedEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;

public class PortServerStoppedEvent extends PortServerLifecycleEvent<ServerStoppedEvent> {
    @Diff
    public PortServerStoppedEvent(ServerStoppedEvent e) {
        super(e);
    }

    static {
        PortEventHooks.register(ServerStoppedEvent.class, PortServerStoppedEvent.class, PortServerStoppedEvent::new);
    }
}
