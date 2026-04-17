package org.mesdag.portlib.event.entity.client.event;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.RegisterNamedRenderTypesEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortRegisterNamedRenderTypesEvent extends PortEvent {
    private final RegisterNamedRenderTypesEvent e;

    @Diff
    public PortRegisterNamedRenderTypesEvent(RegisterNamedRenderTypesEvent e) {
        super(e);
        this.e = e;
    }

    public void register(ResourceLocation key, RenderType blockRenderType, RenderType entityRenderType) {
        e.register(key, blockRenderType, entityRenderType);
    }

    public void register(ResourceLocation key, RenderType blockRenderType, RenderType entityRenderType, RenderType fabulousEntityRenderType) {
        e.register(key, blockRenderType, entityRenderType, fabulousEntityRenderType);
    }

    static {
        PortEventHooks.register(RegisterNamedRenderTypesEvent.class, PortRegisterNamedRenderTypesEvent.class, PortRegisterNamedRenderTypesEvent::new);
    }
}