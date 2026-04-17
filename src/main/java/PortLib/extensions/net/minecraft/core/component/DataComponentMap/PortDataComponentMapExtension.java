package PortLib.extensions.net.minecraft.core.component.DataComponentMap;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.minecraft.core.component.DataComponentMap;
import org.mesdag.portlib.component.PortDataComponentMap;
import org.mesdag.portlib.diff.Diff;

@Extension
public class PortDataComponentMapExtension {
    @Diff
    public static PortDataComponentMap wrap(@This DataComponentMap thiz) {
        return new PortDataComponentMap.Delegate(thiz);
    }
}
