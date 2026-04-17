package PortLib.extensions.net.minecraft.resources.ResourceKey;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

@Extension
public class PortResourceKeyExtension {
    public static <T> ResourceKey<Registry<T>> registryKey(@This ResourceKey<T> thiz) {
        return ResourceKey.createRegistryKey(thiz.registry());
    }
}
