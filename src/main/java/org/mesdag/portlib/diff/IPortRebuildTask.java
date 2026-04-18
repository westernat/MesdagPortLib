package org.mesdag.portlib.diff;

import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import org.mesdag.portlib.event.client.PortAddSectionGeometryEvent;

import java.util.List;

public interface IPortRebuildTask {
    void portlib$setAdditionalRenderers(List<PortAddSectionGeometryEvent.PortAdditionalSectionRenderer> additionalRenderers);

    static IPortRebuildTask of(ChunkRenderDispatcher.RenderChunk.RebuildTask task) {
        return (IPortRebuildTask) task;
    }
}
