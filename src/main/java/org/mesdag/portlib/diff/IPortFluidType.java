package org.mesdag.portlib.diff;

import net.minecraftforge.fluids.FluidType;
import org.mesdag.portlib.diff.mixin.IPortClientExtensionsSetter;

public interface IPortFluidType extends IPortClientExtensionsSetter {
    static IPortFluidType of(FluidType type) {
        return (IPortFluidType) type;
    }
}
