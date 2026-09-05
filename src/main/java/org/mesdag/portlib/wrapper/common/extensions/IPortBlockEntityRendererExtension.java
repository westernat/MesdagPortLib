package org.mesdag.portlib.wrapper.common.extensions;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("all")
public interface IPortBlockEntityRendererExtension<T extends BlockEntity> {
    default AABB getRenderBoundingBox(T blockEntity) {
        return blockEntity.getRenderBoundingBox();
    }

    @ApiStatus.Internal
    static <T extends BlockEntity> @Nullable AABB getRenderBoundingBoxNeo(BlockEntityRenderDispatcher dispatcher, T blockEntity) {
        BlockEntityRenderer<T> renderer = dispatcher.getRenderer(blockEntity);
        if (renderer != null) {
            return of(renderer).getRenderBoundingBox(blockEntity);
        }
        return null;
    }

    static <T extends BlockEntity> IPortBlockEntityRendererExtension<T> of(Object renderer) {
        return (IPortBlockEntityRendererExtension<T>) renderer;
    }
}
