package org.mesdag.portlib.event.entity.player;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.event.entity.player.PlayerContainerEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;

public class PortPlayerContainerEvent extends PortPlayerEvent<PlayerContainerEvent> {
    @Diff
    public PortPlayerContainerEvent(PlayerContainerEvent e) {
        super(e);
    }

    public AbstractContainerMenu getContainer() {
        return e.getContainer();
    }

    public static class PortOpen extends PortPlayerContainerEvent {
        @Diff
        public PortOpen(PlayerContainerEvent.Open e) {
            super(e);
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class PortClose extends PortPlayerContainerEvent {
        @Diff
        public PortClose(PlayerContainerEvent.Close e) {
            super(e);
        }

        static {
            PortEventHooks.register();
        }
    }
}
