package org.mesdag.portlib.registries;

import com.google.common.base.Supplier;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.mesdag.portlib.wrapper.resources.PortIdentifier;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public class PortRegistration<T> {
    static final Map<ResourceKey<? extends Registry<?>>, List<PortRegistryEntry<?>>> registrations = new IdentityHashMap<>();
    final String namespace;
    final ResourceKey<? extends Registry<T>> registryKey;
    final List<PortRegistryEntry<?>> entries;

    private Registry<T> registry;

    PortRegistration(String namespace, ResourceKey<? extends Registry<T>> registryKey) {
        this.namespace = namespace;
        this.registryKey = registryKey;
        this.entries = new ArrayList<>();
        registrations.put(registryKey, entries);
    }

    protected PortIdentifier asId(String name) {
        return PortIdentifier.fromNamespaceAndPath(namespace, name);
    }

    @SuppressWarnings("unchecked")
    public <R extends T> PortRegistryEntry<R> register(String name, Supplier<R> valueSupplier) {
        PortRegistryEntry<R> entry = new PortRegistryEntry<>(asId(name), valueSupplier);
        entry.object = (DeferredHolder<R, R>) DeferredHolder.create(registryKey, entry.identifier);
        entries.add(entry);
        return entry;
    }

    public ResourceKey<? extends Registry<T>> key() {
        return registryKey;
    }

    @SuppressWarnings("unchecked")
    public void addAlias(PortIdentifier from, PortIdentifier to) {
        if (registry == null) {
            this.registry = (Registry<T>) BuiltInRegistries.REGISTRY.getOrThrow((ResourceKey) registryKey);
        }
        registry.addAlias(from, to);
    }
}
