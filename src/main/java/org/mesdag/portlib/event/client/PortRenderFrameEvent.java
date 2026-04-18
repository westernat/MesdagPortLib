package org.mesdag.portlib.event.client;

import net.minecraftforge.event.TickEvent;
import org.mesdag.portlib.client.PortDeltaTicker;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

import java.util.List;

public abstract class PortRenderFrameEvent extends PortEvent<TickEvent.RenderTickEvent> {
    @Diff
    public PortRenderFrameEvent(TickEvent.RenderTickEvent e) {
        super(e);
    }

    public PortDeltaTicker getPartialTick() {
        return PortDeltaTicker.INSTANCE;
    }

    public static class PortPre extends PortRenderFrameEvent {
        @Diff
        public PortPre(TickEvent.RenderTickEvent e) {
            super(e);
        }
    }

    public static class PortPost extends PortRenderFrameEvent {
        @Diff
        public PortPost(TickEvent.RenderTickEvent e) {
            super(e);
        }
    }

    static {
        PortEventHooks.registerCombined(TickEvent.RenderTickEvent.class, List.of(
                PortPre.class,
                PortPost.class
        ), e -> {
            if (e.phase == TickEvent.Phase.START) {
                return new PortPre(e);
            }
            return new PortPost(e);
        });
    }
}
