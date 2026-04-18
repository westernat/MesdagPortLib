package org.mesdag.portlib.event.client;

import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.client.event.RegisterNamedRenderTypesEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.wrapper.resources.PortIdentifier;

public class PortRegisterNamedRenderTypesEvent extends PortEvent<RegisterNamedRenderTypesEvent> {
    @Diff
    public PortRegisterNamedRenderTypesEvent(RegisterNamedRenderTypesEvent e) {
        super(e);
    }

    public void register(PortIdentifier key, RenderType blockRenderType, RenderType entityRenderType) {
        e.register(key.getPath(), blockRenderType, entityRenderType);
    }

    public void register(PortIdentifier key, RenderType blockRenderType, RenderType entityRenderType, RenderType fabulousEntityRenderType) {
        e.register(key.getPath(), blockRenderType, entityRenderType, fabulousEntityRenderType);
    }

    static {
        PortEventHooks.register();
    }
}
