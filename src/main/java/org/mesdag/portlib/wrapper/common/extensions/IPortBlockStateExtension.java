package org.mesdag.portlib.wrapper.common.extensions;

import PortLib.extensions.net.minecraft.world.level.block.state.BlockState.PortBlockStateExtension;
import net.minecraft.world.level.block.state.BlockState;

@SuppressWarnings("all")
public interface IPortBlockStateExtension {

    private BlockState self() {
        return (BlockState) (Object) this;
    }

    default boolean isEmpty() {
        return PortBlockStateExtension.isEmpty(self());
    }

    static IPortBlockStateExtension of(BlockState state) {
        return (IPortBlockStateExtension) (Object) state;
    }
}
