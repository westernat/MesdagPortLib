package org.mesdag.portlib.event.server;

import net.minecraftforge.event.server.ServerAboutToStartEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;

public class PortServerAboutToStartEvent extends PortServerLifecycleEvent<ServerAboutToStartEvent> {
    @Diff
    public PortServerAboutToStartEvent(ServerAboutToStartEvent e) {
        super(e);
    }

    static {
        PortEventHooks.register();
    }
}
