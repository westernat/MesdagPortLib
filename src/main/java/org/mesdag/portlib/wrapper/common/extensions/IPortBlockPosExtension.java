package org.mesdag.portlib.wrapper.common.extensions;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

@SuppressWarnings("all")
public interface IPortBlockPosExtension {
    private BlockPos self() {
        return (BlockPos) this;
    }

    default Vec3 getBottomCenter() {
        return new Vec3(self().getX() + 0.5, self().getY(), self().getZ() + 0.5);
    }

    static IPortBlockPosExtension of(BlockPos pos) {
        return (IPortBlockPosExtension) pos;
    }
}
