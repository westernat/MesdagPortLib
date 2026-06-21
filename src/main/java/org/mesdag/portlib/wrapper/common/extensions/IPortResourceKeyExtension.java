package org.mesdag.portlib.wrapper.common.extensions;

import PortLib.extensions.net.minecraft.resources.ResourceKey.PortResourceKeyExtension;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

@SuppressWarnings("all")
public interface IPortResourceKeyExtension<T> {

    private ResourceKey<T> self() {
        return (ResourceKey<T>) this;
    }

    default ResourceKey<Registry<T>> registryKey() {
        return PortResourceKeyExtension.registryKey(self());
    }

    static <T> IPortResourceKeyExtension<T> of(ResourceKey<T> key) {
        return (IPortResourceKeyExtension<T>) key;
    }
}
