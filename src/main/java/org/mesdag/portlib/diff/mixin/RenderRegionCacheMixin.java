package org.mesdag.portlib.diff.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.renderer.chunk.RenderRegionCache;
import org.mesdag.portlib.diff.IPortRenderRegionCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RenderRegionCache.class)
public abstract class RenderRegionCacheMixin implements IPortRenderRegionCache {
    @Unique
    private boolean portlib$nullForEmpty = true;

    @Override
    public void portlib$setNullForEmpty(boolean value) {
        this.portlib$nullForEmpty = value;
    }

    @ModifyExpressionValue(method = "createRegion", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/chunk/RenderRegionCache;isAllEmpty(Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/BlockPos;II[[Lnet/minecraft/client/renderer/chunk/RenderRegionCache$ChunkInfo;)Z"))
    private boolean modify(boolean original) {
        boolean b = original && portlib$nullForEmpty;
        this.portlib$nullForEmpty = true;
        return b;
    }
}
