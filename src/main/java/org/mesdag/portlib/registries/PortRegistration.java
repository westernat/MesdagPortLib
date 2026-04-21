package org.mesdag.portlib.registries;

import com.google.common.base.Supplier;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.mesdag.portlib.wrapper.resources.PortIdentifier;

import java.util.ArrayList;
import java.util.List;

public class PortRegistration<T> {
    static final Multimap<ResourceKey<? extends Registry<?>>, List<PortRegistryEntry<?, ?>>> registrations = HashMultimap.create();
    final String namespace;
    final ResourceKey<? extends Registry<T>> registryKey;
    final List<PortRegistryEntry<?, ?>> entries;

    private Registry<T> registry;

    PortRegistration(String namespace, ResourceKey<? extends Registry<T>> registryKey, boolean registerEntries) {
        this.namespace = namespace;
        this.registryKey = registryKey;
        this.entries = new ArrayList<>();
        if (registerEntries) {
            registrations.put(registryKey, entries);
        }
    }

    PortRegistration(String namespace, ResourceKey<? extends Registry<T>> registryKey) {
        this(namespace, registryKey, true);
    }

    protected PortIdentifier asId(String name) {
        return PortIdentifier.fromNamespaceAndPath(namespace, name);
    }

    public <R extends T> PortRegistryEntry<T, R> register(String name, Supplier<R> valueSupplier) {
        PortRegistryEntry<T, R> entry = new PortRegistryEntry<>(asId(name), valueSupplier);
        entry.object = DeferredHolder.create(registryKey, entry.identifier);
        entries.add(entry);
        return entry;
    }

    public ResourceKey<? extends Registry<T>> key() {
        return registryKey;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void addAlias(PortIdentifier from, PortIdentifier to) {
        if (registry == null) {
            this.registry = (Registry<T>) BuiltInRegistries.REGISTRY.getOrThrow((ResourceKey) registryKey);
        }
        registry.addAlias(from, to);
    }

    @SuppressWarnings("unchecked")
    public <R extends T> List<PortRegistryEntry<T, R>> getEntries() {
        return (List<PortRegistryEntry<T, R>>) (List<?>) entries;
    }
}
