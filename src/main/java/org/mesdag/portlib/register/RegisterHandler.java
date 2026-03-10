package org.mesdag.portlib.register;

import com.google.common.base.Supplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.registries.RegisterEvent;
import org.mesdag.portlib.PortLib;
import org.mesdag.portlib.event.EventHandler;
import org.mesdag.portlib.event.Priority;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class RegisterHandler {
    private static final Map<ResourceKey<? extends Registry<?>>, List<RegistryEntry<?, ?>>> registrations = new IdentityHashMap<>();
    private static boolean initialized;

    public static <T, R extends Registry<T>> Registration<T, R> registration(String namespace, ResourceKey<R> registryKey) {
        return new Registration<>(namespace, registryKey);
    }

    @SuppressWarnings("unchecked")
    public static <T, R extends Registry<T>> void init() {
        if (initialized) {
            throw new IllegalStateException();
        }
        EventHandler.addListener(Priority.LOWEST, (RegisterEvent event) -> {
            List<RegistryEntry<?, ?>> entries = registrations.get(event.getRegistryKey());
            if (entries == null) return;
            for (RegistryEntry<?, ?> entry : entries) {
                event.register((ResourceKey<R>) entry.registryKey, entry.identifier, (Supplier<T>) entry.valueSupplier.get());
            }
        });
        initialized = true;
    }

    public static class Registration<T, R extends Registry<T>> {
        final String namespace;
        final ResourceKey<R> registryKey;
        final List<RegistryEntry<?, ?>> entries;

        Registration(String namespace, ResourceKey<R> registryKey) {
            this.namespace = namespace;
            this.registryKey = registryKey;
            this.entries = new ArrayList<>();
            registrations.put(registryKey, entries);
        }

        public RegistryEntry<T, R> register(String name, Supplier<T> valueSupplier) {
            RegistryEntry<T, R> entry = new RegistryEntry<>(registryKey, PortLib.identifier(namespace, name), valueSupplier);
            entries.add(entry);
            return entry;
        }
    }
}
