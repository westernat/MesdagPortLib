package PortLib.extensions.net.minecraft.resources.ResourceKey;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class PortResourceKeyExtension {
    public static <T> ResourceKey<Registry<T>> registryKey(ResourceKey<T> thiz) {
        return ResourceKey.createRegistryKey(thiz.registry());
    }
}
