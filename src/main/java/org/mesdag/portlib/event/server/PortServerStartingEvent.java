package org.mesdag.portlib.event.server;

import net.minecraftforge.event.server.ServerStartingEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;

public class PortServerStartingEvent extends PortServerLifecycleEvent<ServerStartingEvent> {
    @Diff
    public PortServerStartingEvent(ServerStartingEvent e) {
        super(e);
    }

    static {
        PortEventHooks.register(ServerStartingEvent.class, PortServerStartingEvent.class, PortServerStartingEvent::new);
    }
}
