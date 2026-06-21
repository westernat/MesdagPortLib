package org.mesdag.portlib.wrapper.common.extensions;

import PortLib.extensions.net.minecraft.world.level.block.state.properties.DoubleBlockHalf.PortDoubleBlockHalfExtension;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

@SuppressWarnings("all")
public interface IPortDoubleBlockHalfExtension {

    private DoubleBlockHalf self() {
        return (DoubleBlockHalf) (Object) this;
    }

    default Direction getDirectionToOther() {
        return PortDoubleBlockHalfExtension.getDirectionToOther(self());
    }

    static IPortDoubleBlockHalfExtension of(DoubleBlockHalf half) {
        return (IPortDoubleBlockHalfExtension) (Object) half;
    }
}
