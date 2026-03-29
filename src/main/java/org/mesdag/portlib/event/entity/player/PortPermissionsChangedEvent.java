package org.mesdag.portlib.event.entity.player;

import net.neoforged.neoforge.event.entity.player.PermissionsChangedEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortPermissionsChangedEvent extends PortPlayerEvent<PermissionsChangedEvent> implements IPortCancellableEvent {
    @Diff
    public PortPermissionsChangedEvent(PermissionsChangedEvent e) {
        super(e);
    }

    public int getNewLevel() {
        return e.getNewLevel();
    }

    public int getOldLevel() {
        return e.getOldLevel();
    }

    static {
        PortEventHooks.register(PermissionsChangedEvent.class, PortPermissionsChangedEvent.class, PortPermissionsChangedEvent::new);
    }
}
