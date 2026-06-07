package org.mesdag.portlib.diff;

import net.minecraftforge.fluids.FluidType;

public interface IPortFluidType extends IPortClientExtensionsSetter {
    static IPortFluidType of(FluidType type) {
        return (IPortFluidType) type;
    }
}
