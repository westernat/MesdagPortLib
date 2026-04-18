package org.mesdag.portlib.event.registries;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.core.PortRegistry;

import java.util.Objects;

public class PortModifyRegistriesEvent extends Event implements IModBusEvent {
    @Diff
    public PortModifyRegistriesEvent() {}

    public Iterable<PortRegistry<?>> getRegistries() {
        return BuiltInRegistries.REGISTRY.registryKeySet().stream()
                .map(key -> RegistryManager.ACTIVE.getRegistry(key.location()))
                .filter(Objects::nonNull)
                .<PortRegistry<?>>map(IForgeRegistry::wrap)
                .toList();
    }

    public <T> PortRegistry<T> getRegistry(ResourceKey<? extends Registry<T>> key) {
        IForgeRegistry<T> registry = RegistryManager.ACTIVE.getRegistry(key);
        if (registry == null) {
            throw new IllegalArgumentException("No registry with key " + key);
        }
        return (PortRegistry<T>) registry.wrap();
    }
}
