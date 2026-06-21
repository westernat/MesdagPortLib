package PortLib.extensions.net.minecraftforge.fluids.FluidStack;

import net.minecraftforge.fluids.FluidStack;
import org.mesdag.portlib.util.Static;

public class PortFluidStackExtension {
    @Static
    public static boolean isSameFluidSameComponents(FluidStack first, FluidStack second) {
        return first.isFluidStackIdentical(second);
    }
}
