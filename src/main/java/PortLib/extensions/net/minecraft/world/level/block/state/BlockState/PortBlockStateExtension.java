package PortLib.extensions.net.minecraft.world.level.block.state.BlockState;

import PortLib.extensions.net.minecraft.world.level.block.Block.PortBlockExtension;
import net.minecraft.world.level.block.state.BlockState;

public class PortBlockStateExtension {
    public static boolean isEmpty(BlockState thiz) {
        return PortBlockExtension.isEmpty(thiz.getBlock(), thiz);
    }
}
