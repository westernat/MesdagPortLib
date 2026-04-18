package org.mesdag.portlib.event.client;

import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.eventbus.api.Event;
import org.mesdag.portlib.diff.Diff;

import java.util.SortedMap;

public class PortRegisterRenderBuffersEvent extends Event {
    private final SortedMap<RenderType, BufferBuilder> renderBuffers;

    @Diff
    public PortRegisterRenderBuffersEvent(SortedMap<RenderType, BufferBuilder> renderBuffers) {
        this.renderBuffers = renderBuffers;
    }

    public void registerRenderBuffer(RenderType renderType) {
        registerRenderBuffer(renderType, new BufferBuilder(renderType.bufferSize()));
    }

    public void registerRenderBuffer(RenderType renderType, BufferBuilder renderBuffer) {
        if (renderBuffers.containsKey(renderType)) {
            throw new IllegalStateException("Duplicate attempt to register render buffer: " + renderType);
        }
        renderBuffers.put(renderType, renderBuffer);
    }
}
