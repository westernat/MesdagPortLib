package org.mesdag.portlib.registries;

import com.google.common.base.Supplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.mesdag.portlib.wrapper.resources.PortIdentifier;

import java.util.ArrayList;
import java.util.List;

public class PortRegistration<T> {
    final String namespace;
    final ResourceKey<? extends Registry<T>> registryKey;
    final List<PortRegistryEntry<?>> entries;

    PortRegistration(String namespace, ResourceKey<? extends Registry<T>> registryKey) {
        this.namespace = namespace;
        this.registryKey = registryKey;
        this.entries = new ArrayList<>();
        PortRegisterHandler.registrations.put(registryKey, entries);
    }

    protected PortIdentifier asId(String name) {
        return PortIdentifier.fromNamespaceAndPath(namespace, name);
    }

    public PortRegistryEntry<T> register(String name, Supplier<T> valueSupplier) {
        PortRegistryEntry<T> entry = new PortRegistryEntry<>(asId(name), valueSupplier);
        entry.object = DeferredHolder.create(registryKey, entry.identifier);
        entries.add(entry);
        return entry;
    }

    public ResourceKey<? extends Registry<T>> key() {
        return registryKey;
    }
}
