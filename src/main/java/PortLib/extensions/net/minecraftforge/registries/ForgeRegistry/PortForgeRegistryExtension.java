package PortLib.extensions.net.minecraftforge.registries.ForgeRegistry;

import net.minecraftforge.registries.ForgeRegistry;
import org.mesdag.portlib.diff.mixin.ForgeRegistryAccessor;

public class PortForgeRegistryExtension {
    public static <V> V byIdOrThrow(ForgeRegistry<V> thiz, int id) {
        V t = ((ForgeRegistryAccessor<V>) thiz).getIds().get(id);
        if (t == null) {
            throw new IllegalArgumentException("No value with id " + id);
        }
        return t;
    }
}
