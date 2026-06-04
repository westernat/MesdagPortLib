package PortLib.extensions.net.minecraftforge.registries.IForgeRegistry;

import net.minecraftforge.registries.IForgeRegistry;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.core.PortRegistry;

public class PortIForgeRegistryExtension {
    @Diff
    public static PortRegistry<?> wrap(IForgeRegistry<?> thiz) {
        return new PortRegistry<>(thiz);
    }
}
