package org.mesdag.portlib.registries;

import com.google.common.base.Supplier;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegistryManager;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@SuppressWarnings("UnstableApiUsage")
public class PortRegistration<T> {
    static final Map<ResourceKey<? extends Registry<?>>, List<List<PortRegistryEntry<?, ?>>>> registrations = new Reference2ObjectOpenHashMap<>();
    final String namespace;
    final ResourceKey<? extends Registry<T>> registryKey;
    final List<PortRegistryEntry<?, ?>> entries;

    private ForgeRegistry<T> registry;

    PortRegistration(String namespace, ResourceKey<? extends Registry<T>> registryKey, boolean registerEntries) {
        this.namespace = namespace;
        this.registryKey = registryKey;
        this.entries = new ArrayList<>();
        if (registerEntries) {
            registrations.computeIfAbsent(registryKey, key -> new ArrayList<>()).add(entries);
        }
    }

    PortRegistration(String namespace, ResourceKey<? extends Registry<T>> registryKey) {
        this(namespace, registryKey, true);
    }

    protected ResourceLocation asId(String name) {
        return ResourceLocation.fromNamespaceAndPath(namespace, name);
    }

    public <R extends T> PortRegistryEntry<T, R> register(String name, Supplier<R> valueSupplier) {
        ResourceLocation id = asId(name);
        PortRegistryEntry<T, R> entry = new PortRegistryEntry<>(id, valueSupplier);
        entry.object = RegistryObject.create(id, registryKey, namespace);
        entries.add(entry);
        return entry;
    }

    public <R extends T> PortRegistryEntry<T, R> register(String name, Function<ResourceLocation, R> valueFunction) {
        ResourceLocation id = asId(name);
        PortRegistryEntry<T, R> entry = new PortRegistryEntry<>(id, () -> valueFunction.apply(id));
        entry.object = RegistryObject.create(id, registryKey, namespace);
        entries.add(entry);
        return entry;
    }

    public ResourceKey<? extends Registry<T>> key() {
        return registryKey;
    }

    public void addAlias(ResourceLocation from, ResourceLocation to) {
        if (registry == null) {
            this.registry = RegistryManager.ACTIVE.getRegistry(registryKey);
        }
        boolean locked = registry.isLocked();
        if (locked) registry.unfreeze();
        registry.addAlias(from, to);
        if (locked) registry.freeze();
    }

    @SuppressWarnings("unchecked")
    public <R extends T> List<PortRegistryEntry<T, R>> getEntries() {
        return (List<PortRegistryEntry<T, R>>) (List<?>) entries;
    }
}
