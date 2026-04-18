package org.mesdag.portlib.event.server;

import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;

public class PortServerStoppingEvent extends PortServerLifecycleEvent<ServerStoppingEvent> {
    @Diff
    public PortServerStoppingEvent(ServerStoppingEvent e) {
        super(e);
    }

    static {
        PortEventHooks.register();
    }
}
