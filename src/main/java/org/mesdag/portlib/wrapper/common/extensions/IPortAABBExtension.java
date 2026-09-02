package org.mesdag.portlib.wrapper.common.extensions;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

@SuppressWarnings("all")
public interface IPortAABBExtension {
    private AABB self() {
        return (AABB) (Object) this;
    }

    default Vec3 getMinPosition() {
        return new Vec3(self().minX, self().minY, self().minZ);
    }

    default Vec3 getMaxPosition() {
        return new Vec3(self().maxX, self().maxY, self().maxZ);
    }

    static AABB encapsulatingFullBlocks(BlockPos startPos, BlockPos endPos) {
        return new AABB(
                Math.min(startPos.getX(), endPos.getX()),
                Math.min(startPos.getY(), endPos.getY()),
                Math.min(startPos.getZ(), endPos.getZ()),
                Math.max(startPos.getX(), endPos.getX()) + 1,
                Math.max(startPos.getY(), endPos.getY()) + 1,
                Math.max(startPos.getZ(), endPos.getZ()) + 1
        );
    }

    static IPortAABBExtension of(AABB aabb) {
        return (IPortAABBExtension) (Object) aabb;
    }
}
