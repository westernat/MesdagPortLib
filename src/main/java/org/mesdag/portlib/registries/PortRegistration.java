package org.mesdag.portlib.registries;

import com.google.common.base.Supplier;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegistryManager;
import net.minecraftforge.registries.RegistryObject;
import org.mesdag.portlib.diff.IPortMappedRegistry;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@SuppressWarnings("UnstableApiUsage")
public class PortRegistration<R> {
    static final Map<String, Map<ResourceKey<? extends Registry<?>>, List<List<PortRegistryEntry<?, ?>>>>> registrations = new ConcurrentHashMap<>();
    final String namespace;
    final ResourceKey<? extends Registry<R>> registryKey;
    final List<PortRegistryEntry<?, ?>> entries;

    PortRegistration(String namespace, ResourceKey<? extends Registry<R>> registryKey, boolean registerEntries) {
        this.namespace = namespace;
        this.registryKey = registryKey;
        this.entries = new ObjectArrayList<>();
        if (registerEntries) {
            registrations.computeIfAbsent(namespace, modId -> new Reference2ObjectOpenHashMap<>())
                    .computeIfAbsent(registryKey, key -> new ObjectArrayList<>())
                    .add(entries);
        }
    }

    PortRegistration(String namespace, ResourceKey<? extends Registry<R>> registryKey) {
        this(namespace, registryKey, true);
    }

    protected ResourceLocation asId(String name) {
        return ResourceLocation.fromNamespaceAndPath(namespace, name);
    }

    public <T extends R> PortRegistryEntry<R, T> register(String name, Supplier<T> valueSupplier) {
        ResourceLocation id = asId(name);
        PortRegistryEntry<R, T> entry = new PortRegistryEntry<>(id, valueSupplier);
        entry.object = RegistryObject.create(id, registryKey, namespace);
        entries.add(entry);
        return entry;
    }

    public <T extends R> PortRegistryEntry<R, T> register(String name, Function<ResourceLocation, T> valueFunction) {
        ResourceLocation id = asId(name);
        PortRegistryEntry<R, T> entry = new PortRegistryEntry<>(id, () -> valueFunction.apply(id));
        entry.object = RegistryObject.create(id, registryKey, namespace);
        entries.add(entry);
        return entry;
    }

    public ResourceKey<? extends Registry<R>> key() {
        return registryKey;
    }

    public void addAlias(ResourceLocation from, ResourceLocation to) {
        ForgeRegistry<R> forgeRegistry = RegistryManager.ACTIVE.getRegistry(registryKey);
        boolean locked = forgeRegistry.isLocked();
        if (locked) forgeRegistry.unfreeze();
        forgeRegistry.addAlias(from, to);
        if (locked) forgeRegistry.freeze();

        Registry<?> vanillaRegistry = BuiltInRegistries.REGISTRY.get(registryKey.location());
        if (vanillaRegistry instanceof IPortMappedRegistry<?> port) {
            port.portlib$addAlias(from, to);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends R> List<PortRegistryEntry<R, T>> getEntries() {
        return (List<PortRegistryEntry<R, T>>) (List<?>) entries;
    }
}
