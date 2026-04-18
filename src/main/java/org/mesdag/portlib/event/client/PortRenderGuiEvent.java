package org.mesdag.portlib.event.client;

import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import org.mesdag.portlib.client.PortDeltaTicker;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public abstract class PortRenderGuiEvent<E extends RenderGuiEvent> extends PortEvent<E> {
    @Diff
    public PortRenderGuiEvent(E e) {
        super(e);
    }

    public GuiGraphics getGuiGraphics() {
        return e.getGuiGraphics();
    }

    public PortDeltaTicker getPartialTick() {
        return e.getPartialTick().wrap();
    }

    public static class PortPre extends PortRenderGuiEvent<RenderGuiEvent.Pre> implements IPortCancellableEvent {
        @Diff
        public PortPre(RenderGuiEvent.Pre e) {
            super(e);
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class PortPost extends PortRenderGuiEvent<RenderGuiEvent.Post> {
        @Diff
        public PortPost(RenderGuiEvent.Post e) {
            super(e);
        }

        static {
            PortEventHooks.register();
        }
    }
}
