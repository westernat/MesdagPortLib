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

    public static class Pre extends PortClientTickEvent {
        @Diff
        public Pre(TickEvent.ClientTickEvent e) {
            super(e);
        }

        static {
            PortEventHooks.registerPredicated(TickEvent.ClientTickEvent.class, Pre.class, Pre::new, event -> event.phase == TickEvent.Phase.START);
        }
    }

    public static class Post extends PortClientTickEvent {
        @Diff
        public Post(TickEvent.ClientTickEvent e) {
            super(e);
        }

        static {
            PortEventHooks.registerPredicated(TickEvent.ClientTickEvent.class, Post.class, Post::new, event -> event.phase == TickEvent.Phase.END);
        }
    }
}
