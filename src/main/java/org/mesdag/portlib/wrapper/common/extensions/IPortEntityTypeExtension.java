package org.mesdag.portlib.wrapper.common.extensions;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.AABB;
import org.mesdag.portlib.diff.IPortEntityType;

public interface IPortEntityTypeExtension<T extends Entity> {
    @SuppressWarnings("unchecked")
    private EntityType<T> self() {
        return (EntityType<T>) this;
    }

    default AABB getSpawnAABB(double x, double y, double z) {
        float scale = ((IPortEntityType<T>) this).portlib$getSpawnDimensionsScale();
        EntityType<T> self = self();
        float halfWidth = scale * self.getWidth() * 05F;
        float height = scale * self.getHeight();
        return new AABB(x - halfWidth, y, z - halfWidth, x + halfWidth, y + height, z + halfWidth);
    }
}
