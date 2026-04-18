package org.mesdag.portlib.event.client;

import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import org.mesdag.portlib.client.GuiLayer;
import org.mesdag.portlib.client.PortDeltaTicker;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.wrapper.resources.PortIdentifier;

public abstract class PortRenderGuiLayerEvent<E extends RenderGuiLayerEvent> extends PortEvent<E> {
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
        return e.getPartialTick().wrap();
    }

    public PortIdentifier getName() {
        if (identifier == null) {
            this.identifier = e.getName().wrap();
        }
        return identifier;
    }

    public GuiLayer getLayer() {
        if (layer == null) {
            this.layer = e.getLayer().wrap();
        }
        return layer;
    }

    public static class PortPre extends PortRenderGuiLayerEvent<RenderGuiLayerEvent.Pre> implements IPortCancellableEvent {
        @Diff
        public PortPre(RenderGuiLayerEvent.Pre e) {
            super(e);
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class PortPost extends PortRenderGuiLayerEvent<RenderGuiLayerEvent.Post> {
        @Diff
        public PortPost(RenderGuiLayerEvent.Post e) {
            super(e);
        }

        static {
            PortEventHooks.register();
        }
    }
}
