package org.mesdag.portlib.wrapper.common.extensions;

import PortLib.extensions.net.minecraft.world.level.block.Block.PortBlockExtension;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

@SuppressWarnings("all")
public interface IPortBlockExtension {

    private Block self() {
        return (Block) this;
    }

    default boolean isEmpty(BlockState state) {
        return PortBlockExtension.isEmpty(self(), state);
    }

    static IPortBlockExtension of(Block block) {
        return (IPortBlockExtension) block;
    }
}
