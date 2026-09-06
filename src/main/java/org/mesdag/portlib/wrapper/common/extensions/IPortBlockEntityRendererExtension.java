package org.mesdag.portlib.wrapper.common.extensions;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;

@SuppressWarnings("all")
public interface IPortBlockEntityRendererExtension<T extends BlockEntity> {
    default AABB getRenderBoundingBox(T blockEntity) {
        return blockEntity.getRenderBoundingBox();
    }

    static <T extends BlockEntity> IPortBlockEntityRendererExtension<T> of(Object renderer) {
        return (IPortBlockEntityRendererExtension<T>) renderer;
    }
}
