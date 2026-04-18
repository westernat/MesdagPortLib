package org.mesdag.portlib.event.client;

import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import org.mesdag.portlib.client.GuiLayer;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortModBusEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.wrapper.resources.PortIdentifier;

import java.util.function.UnaryOperator;

public class PortRegisterGuiLayersEvent extends PortEvent<RegisterGuiLayersEvent> implements IPortModBusEvent {
    @Diff
    public PortRegisterGuiLayersEvent(RegisterGuiLayersEvent e) {
        super(e);
    }

    public void registerBelowAll(PortIdentifier id, GuiLayer layer) {
        e.registerBelowAll(id, layer.unwrap());
    }

    public void registerBelow(PortIdentifier other, PortIdentifier id, GuiLayer layer) {
        e.registerBelow(other, id, layer.unwrap());
    }

    public void registerAbove(PortIdentifier other, PortIdentifier id, GuiLayer layer) {
        e.registerAbove(other, id, layer.unwrap());
    }

    public void registerAboveAll(PortIdentifier id, GuiLayer layer) {
        e.registerAboveAll(id, layer.unwrap());
    }

    public void replaceLayer(PortIdentifier id, GuiLayer replacement) {
        e.replaceLayer(id, replacement.unwrap());
    }

    public void wrapLayer(PortIdentifier id, UnaryOperator<GuiLayer> wrapper) {
        e.wrapLayer(id, layer -> wrapper.apply(layer.wrap()).unwrap());
    }

    static {
        PortEventHooks.register();
    }
}
