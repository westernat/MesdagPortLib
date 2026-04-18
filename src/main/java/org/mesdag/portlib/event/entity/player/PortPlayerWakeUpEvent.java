package org.mesdag.portlib.event.entity.player;

import net.neoforged.neoforge.event.entity.player.PlayerWakeUpEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;

public class PortPlayerWakeUpEvent extends PortPlayerEvent<PlayerWakeUpEvent> {
    @Diff
    public PortPlayerWakeUpEvent(PlayerWakeUpEvent e) {
        super(e);
    }

    public boolean wakeImmediately() {
        return e.wakeImmediately();
    }

    public boolean updateLevel() {
        return e.updateLevel();
    }

    static {
        PortEventHooks.register();
    }
}
