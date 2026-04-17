package PortLib.extensions.net.minecraft.core.component.DataComponentType;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.minecraft.core.component.DataComponentType;
import org.mesdag.portlib.component.PortDataComponentType;
import org.mesdag.portlib.diff.Diff;

@Extension
public class PortDataComponentTypeExtension {
    @Diff
    public static <T> PortDataComponentType<T> wrap(@This DataComponentType<T> thiz) {
        return new PortDataComponentType<>(thiz);
    }
}
