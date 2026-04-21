package org.mesdag.portlib.event.client;

import net.minecraftforge.event.TickEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public abstract class PortClientTickEvent extends PortEvent<TickEvent.ClientTickEvent> {
    @Diff
    public PortClientTickEvent(TickEvent.ClientTickEvent e) {
        super(e);
    }

    public static class PortPre extends PortClientTickEvent {
        @Diff
        public PortPre(TickEvent.ClientTickEvent e) {
            super(e);
        }

        static {
            PortEventHooks.registerPredicated(TickEvent.ClientTickEvent.class, PortPre.class, PortPre::new, event -> event.phase == TickEvent.Phase.START);
        }
    }

    public static class PortPost extends PortClientTickEvent {
        @Diff
        public PortPost(TickEvent.ClientTickEvent e) {
            super(e);
        }

        static {
            PortEventHooks.registerPredicated(TickEvent.ClientTickEvent.class, PortPost.class, PortPost::new, event -> event.phase == TickEvent.Phase.END);
        }
    }
}
