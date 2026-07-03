package PortLib.extensions.net.minecraft.world.phys.AABB;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class PortAABBExtension {
    public static Vec3 getMinPosition(AABB thiz) {
        return new Vec3(thiz.minX, thiz.minY, thiz.minZ);
    }

    public static Vec3 getMaxPosition(AABB thiz) {
        return new Vec3(thiz.maxX, thiz.maxY, thiz.maxZ);
    }

    public static AABB encapsulatingFullBlocks(BlockPos startPos, BlockPos endPos) {
        return new AABB(
                Math.min(startPos.getX(), endPos.getX()),
                Math.min(startPos.getY(), endPos.getY()),
                Math.min(startPos.getZ(), endPos.getZ()),
                Math.max(startPos.getX(), endPos.getX()) + 1,
                Math.max(startPos.getY(), endPos.getY()) + 1,
                Math.max(startPos.getZ(), endPos.getZ()) + 1
        );
    }
}
