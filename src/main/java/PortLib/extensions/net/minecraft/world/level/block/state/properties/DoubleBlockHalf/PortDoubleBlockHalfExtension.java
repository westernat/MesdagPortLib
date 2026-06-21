package PortLib.extensions.net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

public class PortDoubleBlockHalfExtension {
    public static Direction getDirectionToOther(DoubleBlockHalf thiz) {
        return thiz == DoubleBlockHalf.UPPER ? Direction.DOWN : Direction.UP;
    }
}
