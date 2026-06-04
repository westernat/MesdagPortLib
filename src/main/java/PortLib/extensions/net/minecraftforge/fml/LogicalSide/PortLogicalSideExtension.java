package PortLib.extensions.net.minecraftforge.fml.LogicalSide;

import net.minecraftforge.fml.LogicalSide;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.fml.PortLogicalSide;

public class PortLogicalSideExtension {
    @Diff
    public static PortLogicalSide wrap(LogicalSide thiz) {
        return thiz == LogicalSide.CLIENT ? PortLogicalSide.CLIENT : PortLogicalSide.SERVER;
    }
}
