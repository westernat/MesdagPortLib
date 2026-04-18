package org.mesdag.portlib.event.client;

import net.neoforged.neoforge.client.event.RenderFrameEvent;
import org.mesdag.portlib.client.PortDeltaTicker;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public abstract class PortRenderFrameEvent<E extends RenderFrameEvent> extends PortEvent<E> {
    @Diff
    public PortRenderFrameEvent(E e) {
        super(e);
    }

    public PortDeltaTicker getPartialTick() {
        return e.getPartialTick().wrap();
    }

    public static class PortPre extends PortRenderFrameEvent<RenderFrameEvent.Pre> {
        @Diff
        public PortPre(RenderFrameEvent.Pre e) {
            super(e);
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class PortPost extends PortRenderFrameEvent<RenderFrameEvent.Post> {
        @Diff
        public PortPost(RenderFrameEvent.Post e) {
            super(e);
        }

        static {
            PortEventHooks.register();
        }
    }
}
