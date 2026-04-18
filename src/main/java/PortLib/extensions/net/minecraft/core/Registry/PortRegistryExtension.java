package PortLib.extensions.net.minecraft.core.Registry;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.minecraft.core.Registry;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.core.PortRegistry;

@Extension
public class PortRegistryExtension {
    @Diff
    public static PortRegistry<?> wrap(@This Registry<?> thiz) {
        return new PortRegistry<>(thiz);
    }
}
