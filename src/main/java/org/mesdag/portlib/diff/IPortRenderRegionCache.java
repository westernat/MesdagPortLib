package org.mesdag.portlib.diff;

import net.minecraft.client.renderer.chunk.RenderRegionCache;

public interface IPortRenderRegionCache {
    void portlib$setNullForEmpty(boolean value);

    static IPortRenderRegionCache of(RenderRegionCache cache) {
        return (IPortRenderRegionCache) cache;
    }
}
