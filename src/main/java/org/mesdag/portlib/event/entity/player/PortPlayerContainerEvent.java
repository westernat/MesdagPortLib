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

    public static class Open extends PortPlayerContainerEvent {
        @Diff
        public Open(PlayerContainerEvent.Open e) {
            super(e);
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class Close extends PortPlayerContainerEvent {
        @Diff
        public Close(PlayerContainerEvent.Close e) {
            super(e);
        }

        static {
            PortEventHooks.register();
        }
    }
}
