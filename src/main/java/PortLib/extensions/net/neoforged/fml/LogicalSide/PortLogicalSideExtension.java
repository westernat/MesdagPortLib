package PortLib.extensions.net.neoforged.fml.LogicalSide;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.neoforged.fml.LogicalSide;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.fml.PortLogicalSide;

@Extension
public class PortLogicalSideExtension {
    @Diff
    public static PortLogicalSide wrap(@This LogicalSide thiz) {
        return thiz == LogicalSide.CLIENT ? PortLogicalSide.CLIENT : PortLogicalSide.SERVER;
    }
}
