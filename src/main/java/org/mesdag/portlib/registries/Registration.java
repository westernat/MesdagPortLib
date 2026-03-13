package org.mesdag.portlib.registries;

import com.google.common.base.Supplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.registries.RegistryObject;
import org.mesdag.portlib.wrapper.resources.PortIdentifier;

import java.util.ArrayList;
import java.util.List;

public class Registration<T> {
    final String namespace;
    final ResourceKey<? extends Registry<T>> registryKey;
    final List<PortRegistryEntry<?>> entries;

    Registration(String namespace, ResourceKey<? extends Registry<T>> registryKey) {
        this.namespace = namespace;
        this.registryKey = registryKey;
        this.entries = new ArrayList<>();
        PortRegisterHandler.registrations.put(registryKey, entries);
    }

    public PortRegistryEntry<T> register(String name, Supplier<T> valueSupplier) {
        PortRegistryEntry<T> entry = new PortRegistryEntry<>(PortIdentifier.fromNamespaceAndPath(namespace, name), valueSupplier);
        entry.object = RegistryObject.create(entry.identifier, registryKey, namespace);
        entries.add(entry);
        return entry;
    }
}
