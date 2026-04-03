package org.mesdag.portlib.event.entity.player;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
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
            PortEventHooks.register(PlayerContainerEvent.Open.class, PortOpen.class, PortOpen::new);
        }
    }

    public static class PortClose extends PortPlayerContainerEvent {
        @Diff
        public PortClose(PlayerContainerEvent.Close e) {
            super(e);
        }

        static {
            PortEventHooks.register(PlayerContainerEvent.Close.class, PortClose.class, PortClose::new);
        }
    }
}
