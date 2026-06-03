package PortLib.extensions.net.minecraftforge.registries.ForgeRegistry;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.minecraftforge.registries.ForgeRegistry;
import org.mesdag.portlib.diff.mixin.ForgeRegistryAccessor;

@Extension
public class PortForgeRegistryExtension {
    public static <V> V byIdOrThrow(@This ForgeRegistry<V> thiz, int id) {
        V t = ((ForgeRegistryAccessor<V>) thiz).getIds().get(id);
        if (t == null) {
            throw new IllegalArgumentException("No value with id " + id);
        }
        return t;
    }
}
