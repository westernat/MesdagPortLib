package org.mesdag.portlib.event.tick;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public abstract class PortPlayerTickEvent extends PortEvent<TickEvent.PlayerTickEvent> {
    @Diff
    public PortPlayerTickEvent(TickEvent.PlayerTickEvent e) {
        super(e);
    }

    public Player getEntity() {
        return e.player;
    }

    public static class PortPre extends PortPlayerTickEvent {
        @Diff
        public PortPre(TickEvent.PlayerTickEvent e) {
            super(e);
        }

        static {
            PortEventHooks.registerPredicated(TickEvent.PlayerTickEvent.class, PortPre.class, PortPre::new, event -> event.phase == TickEvent.Phase.START);
        }
    }

    public static class PortPost extends PortPlayerTickEvent {
        @Diff
        public PortPost(TickEvent.PlayerTickEvent e) {
            super(e);
        }

        static {
            PortEventHooks.registerPredicated(TickEvent.PlayerTickEvent.class, PortPost.class, PortPost::new, event -> event.phase == TickEvent.Phase.END);
        }
    }
}
