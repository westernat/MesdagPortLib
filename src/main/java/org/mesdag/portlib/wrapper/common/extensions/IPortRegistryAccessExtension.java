package org.mesdag.portlib.wrapper.common.extensions;

import PortLib.extensions.net.minecraft.core.RegistryAccess.PortRegistryAccessExtension;
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
        return PortRegistryAccessExtension.listRegistries(self());
    }

    static IPortRegistryAccessExtension of(RegistryAccess registryAccess) {
        return (IPortRegistryAccessExtension) registryAccess;
    }
}
