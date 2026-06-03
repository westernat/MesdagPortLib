package org.mesdag.portlib.event.client;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import org.mesdag.portlib.client.GuiLayer;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.diff.mixin.RegisterGuiOverlaysEventAccessor;
import org.mesdag.portlib.event.IPortModBusEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

import java.util.Objects;
import java.util.function.UnaryOperator;

public class PortRegisterGuiLayersEvent extends PortEvent<RegisterGuiOverlaysEvent> implements IPortModBusEvent {
    @Diff
    public PortRegisterGuiLayersEvent(RegisterGuiOverlaysEvent e) {
        super(e);
    }

    public void registerBelowAll(ResourceLocation id, GuiLayer layer) {
        e.registerBelowAll(id.getPath(), layer.unwrap());
    }

    public void registerBelow(ResourceLocation other, ResourceLocation id, GuiLayer layer) {
        e.registerBelow(other, id.getPath(), layer.unwrap());
    }

    public void registerAbove(ResourceLocation other, ResourceLocation id, GuiLayer layer) {
        e.registerAbove(other, id.getPath(), layer.unwrap());
    }

    public void registerAboveAll(ResourceLocation id, GuiLayer layer) {
        e.registerAboveAll(id.getPath(), layer.unwrap());
    }

    public void replaceLayer(ResourceLocation id, GuiLayer replacement) {
        wrapLayer(id, layer -> replacement);
    }

    public void wrapLayer(ResourceLocation id, UnaryOperator<GuiLayer> wrapper) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(wrapper);

        RegisterGuiOverlaysEventAccessor accessor = (RegisterGuiOverlaysEventAccessor) e;

        IGuiOverlay overlay = accessor.getOverlays().get(id);
        if (overlay != null) {
            int i = accessor.getOrderedOverlays().indexOf(id);
            if (i > 0) {
                var wrapped = wrapper.apply(overlay.wrap());
                Objects.requireNonNull(wrapped, "wrapping layer must not be null");
                accessor.getOverlays().put(id, wrapped.unwrap());
            }
        }

        throw new IllegalArgumentException("Attempted to wrap layer with id '" + id + "', which does not exist!");
    }

    static {
        PortEventHooks.register();
    }
}
