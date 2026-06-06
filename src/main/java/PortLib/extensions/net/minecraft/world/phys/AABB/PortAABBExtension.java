package PortLib.extensions.net.minecraft.world.phys.AABB;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class PortAABBExtension {
    public static Vec3 getMinPosition(AABB thiz) {
        return new Vec3(thiz.minX, thiz.minY, thiz.minZ);
    }

    public static Vec3 getMaxPosition(AABB thiz) {
        return new Vec3(thiz.maxX, thiz.maxY, thiz.maxZ);
    }
}
