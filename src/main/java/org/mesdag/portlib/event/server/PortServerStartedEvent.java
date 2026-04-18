package org.mesdag.portlib.event.server;

import net.minecraftforge.event.server.ServerStartedEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;

public class PortServerStartedEvent extends PortServerLifecycleEvent<ServerStartedEvent> {
    @Diff
    public PortServerStartedEvent(ServerStartedEvent e) {
        super(e);
    }

    static {
        PortEventHooks.register();
    }
}
