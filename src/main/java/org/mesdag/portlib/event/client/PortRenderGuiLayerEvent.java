package org.mesdag.portlib.event.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import org.mesdag.portlib.client.GuiLayer;
import org.mesdag.portlib.client.PortDeltaTicker;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.wrapper.resources.PortIdentifier;

public abstract class PortRenderGuiLayerEvent<E extends RenderGuiOverlayEvent> extends PortEvent<E> {
    private PortIdentifier identifier;
    private GuiLayer layer;

    @Diff
    public PortRenderGuiLayerEvent(E e) {
        super(e);
    }

    public GuiGraphics getGuiGraphics() {
        return e.getGuiGraphics();
    }

    public PortDeltaTicker getPartialTick() {
        return PortDeltaTicker.INSTANCE;
    }

    public PortIdentifier getName() {
        if (identifier == null) {
            this.identifier = e.getOverlay().id().wrap();
        }
        return identifier;
    }

    public GuiLayer getLayer() {
        if (layer == null) {
            this.layer = e.getOverlay().overlay().wrap();
        }
        return layer;
    }

    public static class PortPre extends PortRenderGuiLayerEvent<RenderGuiOverlayEvent.Pre> implements IPortCancellableEvent {
        @Diff
        public PortPre(RenderGuiOverlayEvent.Pre e) {
            super(e);
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class PortPost extends PortRenderGuiLayerEvent<RenderGuiOverlayEvent.Post> {
        @Diff
        public PortPost(RenderGuiOverlayEvent.Post e) {
            super(e);
        }

        static {
            PortEventHooks.register();
        }
    }
}
