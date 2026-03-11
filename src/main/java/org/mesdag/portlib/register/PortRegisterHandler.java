package org.mesdag.portlib.register;

import com.google.common.base.Supplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.mesdag.portlib.event.PortEventHandler;
import org.mesdag.portlib.event.PortPriority;
import org.mesdag.portlib.wrapper.PortIdentifier;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("all")
public class PortRegisterHandler {
    private static final Map<ResourceKey<? extends Registry<?>>, List<PortRegistryEntry<?, ?>>> registrations = new IdentityHashMap<>();
    private static boolean initialized;

    public static <T, R extends Registry<T>> Registration<T, R> registration(String namespace, ResourceKey<R> registryKey) {
        return new Registration<>(namespace, registryKey);
    }

    public static <T, R extends Registry<T>> void init() {
        if (initialized) {
            throw new IllegalStateException();
        }
        PortEventHandler.addListener(PortPriority.LOWEST, (RegisterEvent event) -> {
            List<PortRegistryEntry<?, ?>> entries = registrations.get(event.getRegistryKey());
            if (entries == null) return;
            for (PortRegistryEntry<?, ?> entry : entries) {
                event.register((ResourceKey<R>) entry.registryKey, entry.identifier, (Supplier<T>) entry.valueSupplier.get());
            }
        });
        initialized = true;
    }

    public static class Registration<T, R extends Registry<T>> {
        final String namespace;
        final ResourceKey<R> registryKey;
        final List<PortRegistryEntry<?, ?>> entries;

        Registration(String namespace, ResourceKey<R> registryKey) {
            this.namespace = namespace;
            this.registryKey = registryKey;
            this.entries = new ArrayList<>();
            registrations.put(registryKey, entries);
        }

        public PortRegistryEntry<T, R> register(String name, Supplier<T> valueSupplier) {
            PortRegistryEntry<T, R> entry = new PortRegistryEntry<>(registryKey, PortIdentifier.fromNamespaceAndPath(namespace, name), valueSupplier);
            entries.add(entry);
            return entry;
        }
    }
}
