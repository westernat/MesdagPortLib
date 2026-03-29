package org.mesdag.portlib.event.registries;

import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.event.server.PortServerLifecycleEvent;

public class PortServerAboutToStartEvent extends PortServerLifecycleEvent<ServerAboutToStartEvent> {
    @Diff
    public PortServerAboutToStartEvent(ServerAboutToStartEvent e) {
        super(e);
    }

    static {
        PortEventHooks.register(ServerAboutToStartEvent.class, PortServerAboutToStartEvent.class, PortServerAboutToStartEvent::new);
    }
}
