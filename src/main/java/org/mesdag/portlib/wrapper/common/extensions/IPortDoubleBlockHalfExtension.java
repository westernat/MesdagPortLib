package org.mesdag.portlib.wrapper.common.extensions;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

@SuppressWarnings("all")
public interface IPortDoubleBlockHalfExtension {
    private DoubleBlockHalf self() {
        return (DoubleBlockHalf) (Object) this;
    }

    default Direction getDirectionToOther() {
        return self() == DoubleBlockHalf.UPPER ? Direction.DOWN : Direction.UP;
    }

    static IPortDoubleBlockHalfExtension of(DoubleBlockHalf half) {
        return (IPortDoubleBlockHalfExtension) (Object) half;
    }
}
