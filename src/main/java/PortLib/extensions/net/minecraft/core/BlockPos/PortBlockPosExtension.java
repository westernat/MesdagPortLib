package PortLib.extensions.net.minecraft.core.BlockPos;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

public class PortBlockPosExtension {
    public static Vec3 getBottomCenter(BlockPos thiz) {
        return new Vec3(thiz.getX() + 0.5, thiz.getY(), thiz.getZ() + 0.5);
    }
}
