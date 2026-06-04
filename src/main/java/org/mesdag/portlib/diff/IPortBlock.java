package org.mesdag.portlib.diff;

import net.minecraft.world.level.block.Block;
import org.mesdag.portlib.diff.mixin.IPortClientExtensionsSetter;

public interface IPortBlock extends IPortClientExtensionsSetter {
    static IPortBlock of(Block block) {
        return (IPortBlock) block;
    }
}
