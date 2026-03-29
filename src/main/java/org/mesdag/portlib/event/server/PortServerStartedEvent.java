package org.mesdag.portlib.event.server;

import net.neoforged.neoforge.event.server.ServerStartedEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;

public class PortServerStartedEvent extends PortServerLifecycleEvent<ServerStartedEvent> {
    @Diff
    public PortServerStartedEvent(ServerStartedEvent e) {
        super(e);
    }

    static {
        PortEventHooks.register(ServerStartedEvent.class, PortServerStartedEvent.class, PortServerStartedEvent::new);
    }
}
