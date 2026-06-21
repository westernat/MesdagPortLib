package org.mesdag.portlib.wrapper.common.extensions;

import PortLib.extensions.net.minecraft.core.BlockPos.PortBlockPosExtension;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

@SuppressWarnings("all")
public interface IPortBlockPosExtension {

    private BlockPos self() {
        return (BlockPos) this;
    }

    default Vec3 getBottomCenter() {
        return PortBlockPosExtension.getBottomCenter(self());
    }

    static IPortBlockPosExtension of(BlockPos pos) {
        return (IPortBlockPosExtension) pos;
    }
}
