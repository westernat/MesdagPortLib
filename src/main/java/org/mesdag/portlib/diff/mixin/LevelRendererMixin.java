package org.mesdag.portlib.diff.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import org.mesdag.portlib.wrapper.common.extensions.IPortBlockEntityRendererExtension;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {
    @Shadow
    @Final
    private BlockEntityRenderDispatcher blockEntityRenderDispatcher;

    @WrapOperation(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/BlockEntity;getRenderBoundingBox()Lnet/minecraft/world/phys/AABB;", remap = false))
    private AABB wrap(BlockEntity instance, Operation<AABB> original) {
        BlockEntityRenderer<?> renderer = blockEntityRenderDispatcher.getRenderer(instance);
        if (renderer != null) {
            AABB aabb = IPortBlockEntityRendererExtension.of(renderer).getRenderBoundingBox(instance);
            if (aabb != null) return aabb;
        }
        return original.call(instance);
    }
}
