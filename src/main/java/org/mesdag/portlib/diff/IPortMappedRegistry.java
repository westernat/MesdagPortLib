package org.mesdag.portlib.diff;

import net.minecraft.core.MappedRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public interface IPortMappedRegistry<T> {
    void portlib$addAlias(ResourceLocation from, ResourceLocation to);

    ResourceLocation portlib$resolve(ResourceLocation name);

    ResourceKey<T> portlib$resolve(ResourceKey<T> key);

    @SuppressWarnings("unchecked")
    static <T> IPortMappedRegistry<T> of(MappedRegistry<T> registry) {
        return (IPortMappedRegistry<T>) registry;
    }
}
