package org.mesdag.portlib.diff;

import net.minecraft.world.level.block.Block;
import org.mesdag.portlib.wrapper.common.extensions.IPortBlockExtension;

public interface IPortBlock extends IPortClientExtensionsSetter, IPortBlockExtension {
    static IPortBlock of(Block block) {
        return (IPortBlock) block;
    }
}
