package org.mesdag.portlib.wrapper.common.extensions;

import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;

import java.util.stream.Stream;

@SuppressWarnings("all")
public interface IPortRegistryAccessExtension {
    private RegistryAccess self() {
        return (RegistryAccess) this;
    }

    default Stream<ResourceKey<? extends Registry<?>>> listRegistries() {
        return self().registries().map(RegistryAccess.RegistryEntry::key);
    }

    static IPortRegistryAccessExtension of(RegistryAccess registryAccess) {
        return (IPortRegistryAccessExtension) registryAccess;
    }
}
