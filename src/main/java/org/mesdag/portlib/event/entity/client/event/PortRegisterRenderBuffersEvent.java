package org.mesdag.portlib.event.entity.client.event;

import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.neoforge.client.event.RegisterRenderBuffersEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;


public class PortRegisterRenderBuffersEvent extends PortEvent {
    private final RegisterRenderBuffersEvent e;

    @Diff
    public PortRegisterRenderBuffersEvent(RegisterRenderBuffersEvent e) {
        super(e);
        this.e = e;
    }

    public void registerRenderBuffer(RenderType renderType) {
        e.registerRenderBuffer(renderType);
    }

    public void registerRenderBuffer(RenderType renderType, ByteBufferBuilder renderBuffer) {
        e.registerRenderBuffer(renderType, renderBuffer);
    }

    static {
        PortEventHooks.register(RegisterRenderBuffersEvent.class, PortRegisterRenderBuffersEvent.class, PortRegisterRenderBuffersEvent::new);
    }
}