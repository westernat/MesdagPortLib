package org.mesdag.portlib.diff.mixin;

import net.minecraftforge.client.model.lighting.QuadLighter;
import org.spongepowered.asm.mixin.gen.Accessor;

@org.spongepowered.asm.mixin.Mixin(value = net.minecraftforge.client.model.lighting.ForgeModelBlockRenderer.class, remap = false)
public interface ForgeModelBlockRendererAccessor {
    @Accessor
    ThreadLocal<QuadLighter> getFlatLighter();

    @Accessor
    ThreadLocal<QuadLighter> getSmoothLighter();
}
