package org.mesdag.portlib.registries;

import com.google.common.base.Supplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegistryManager;
import net.minecraftforge.registries.RegistryObject;
import org.mesdag.portlib.wrapper.resources.PortIdentifier;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("UnstableApiUsage")
public class PortRegistration<T> {
    static final Map<ResourceKey<? extends Registry<?>>, List<PortRegistryEntry<?>>> registrations = new IdentityHashMap<>();
    final String namespace;
    final ResourceKey<? extends Registry<T>> registryKey;
    final List<PortRegistryEntry<?>> entries;

    private ForgeRegistry<T> registry;

    PortRegistration(String namespace, ResourceKey<? extends Registry<T>> registryKey) {
        this.namespace = namespace;
        this.registryKey = registryKey;
        this.entries = new ArrayList<>();
        registrations.put(registryKey, entries);
    }

    protected PortIdentifier asId(String name) {
        return PortIdentifier.fromNamespaceAndPath(namespace, name);
    }

    public <R extends T> PortRegistryEntry<R> register(String name, Supplier<R> valueSupplier) {
        PortRegistryEntry<R> entry = new PortRegistryEntry<>(asId(name), valueSupplier);
        entry.object = RegistryObject.create(entry.identifier, registryKey, namespace);
        entries.add(entry);
        return entry;
    }

    public ResourceKey<? extends Registry<T>> key() {
        return registryKey;
    }

    public void addAlias(PortIdentifier from, PortIdentifier to) {
        if (registry == null) {
            this.registry = RegistryManager.ACTIVE.getRegistry(registryKey);
        }
        boolean locked = registry.isLocked();
        if (locked) registry.unfreeze();
        registry.addAlias(from, to);
        if (locked) registry.freeze();
    }
}
