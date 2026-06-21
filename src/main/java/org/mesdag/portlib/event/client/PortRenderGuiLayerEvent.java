package org.mesdag.portlib.event.client;

import PortLib.extensions.net.minecraftforge.client.gui.overlay.IGuiOverlay.PortIGuiOverlayExtension;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import org.mesdag.portlib.client.PortDeltaTicker;
import org.mesdag.portlib.client.PortGuiLayer;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public abstract class PortRenderGuiLayerEvent<E extends RenderGuiOverlayEvent> extends PortEvent<E> {
    private ResourceLocation identifier;
    private PortGuiLayer layer;

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

    public ResourceLocation getName() {
        if (identifier == null) {
            this.identifier = e.getOverlay().id();
        }
        return identifier;
    }

    public PortGuiLayer getLayer() {
        if (layer == null) {
            this.layer = PortIGuiOverlayExtension.wrap(e.getOverlay().overlay());
        }
        return layer;
    }

    public static class Pre extends PortRenderGuiLayerEvent<RenderGuiOverlayEvent.Pre> implements IPortCancellableEvent {
        @Diff
        public Pre(RenderGuiOverlayEvent.Pre e) {
            super(e);
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class Post extends PortRenderGuiLayerEvent<RenderGuiOverlayEvent.Post> {
        @Diff
        public Post(RenderGuiOverlayEvent.Post e) {
            super(e);
        }

        static {
            PortEventHooks.register();
        }
    }
}
