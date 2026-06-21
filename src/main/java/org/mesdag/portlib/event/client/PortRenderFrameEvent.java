package org.mesdag.portlib.event.client;

import net.minecraftforge.event.TickEvent;
import org.mesdag.portlib.client.PortDeltaTicker;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public abstract class PortRenderFrameEvent extends PortEvent<TickEvent.RenderTickEvent> {
    @Diff
    public PortRenderFrameEvent(TickEvent.RenderTickEvent e) {
        super(e);
    }

    public PortDeltaTicker getPartialTick() {
        return PortDeltaTicker.INSTANCE;
    }

    public static class Pre extends PortRenderFrameEvent {
        @Diff
        public Pre(TickEvent.RenderTickEvent e) {
            super(e);
        }

        static {
            PortEventHooks.registerPredicated(TickEvent.RenderTickEvent.class, Pre.class, Pre::new, event -> event.phase == TickEvent.Phase.START);
        }
    }

    public static class Post extends PortRenderFrameEvent {
        @Diff
        public Post(TickEvent.RenderTickEvent e) {
            super(e);
        }

        static {
            PortEventHooks.registerPredicated(TickEvent.RenderTickEvent.class, Post.class, Post::new, event -> event.phase == TickEvent.Phase.END);
        }
    }
}
