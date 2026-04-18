package PortLib.extensions.net.minecraftforge.registries.IForgeRegistry;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.minecraftforge.registries.IForgeRegistry;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.core.PortRegistry;

@Extension
public class PortIForgeRegistryExtension {
    @Diff
    public static PortRegistry<?> wrap(@This IForgeRegistry<?> thiz) {
        return new PortRegistry<>(thiz);
    }
}
