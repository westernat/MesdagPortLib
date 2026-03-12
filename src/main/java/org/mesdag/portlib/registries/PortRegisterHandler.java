package org.mesdag.portlib.registries;

import com.google.common.base.Supplier;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.event.PortBus;
import org.mesdag.portlib.event.PortEventHandler;
import org.mesdag.portlib.event.PortPriority;
import org.mesdag.portlib.wrapper.PortIdentifier;

import java.util.*;
import java.util.function.Consumer;

@SuppressWarnings("all")
public class PortRegisterHandler {
    private static final Map<ResourceKey<? extends Registry<?>>, List<PortRegistryEntry<?, ?>>> registrations = new IdentityHashMap<>();

    public static <T, R extends Registry<T>> Registration<T, R> registration(String namespace, ResourceKey<R> registryKey) {
        return new Registration<>(namespace, registryKey);
    }

    public static <T, R extends Registry<T>> CustomRegistration<T, R> custom(String namespace, ResourceKey<R> registryKey, Consumer<PortRegistryMaker<T>> consumer) {
        return new CustomRegistration<>(namespace, registryKey, consumer);
    }

    public static <T, R extends Registry<T>> void init() {
        PortEventHandler.addListener(PortPriority.LOWEST, (RegisterEvent event) -> {
            ResourceKey<? extends Registry<?>> registryKey = event.getRegistryKey();
            List<PortRegistryEntry<?, ?>> entries = registrations.get(registryKey);
            if (entries == null) return;
            for (PortRegistryEntry<?, ?> entry : entries) {
                event.register((ResourceKey<R>) registryKey, entry.identifier, (Supplier<T>) entry.valueSupplier.get());
            }
        });
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
            PortRegistryEntry<T, R> entry = new PortRegistryEntry<>(PortIdentifier.fromNamespaceAndPath(namespace, name), valueSupplier);
            entry.object = RegistryObject.create(entry.identifier, registryKey, namespace);
            entries.add(entry);
            return entry;
        }
    }

    public static class CustomRegistration<T, R extends Registry<T>> extends Registration<T, R> {
        final java.util.function.Supplier<IForgeRegistry<T>> registry;

        CustomRegistration(String namespace, ResourceKey<R> registryKey, Consumer<PortRegistryMaker<T>> consumer) {
            super(namespace, registryKey);
            DeferredRegister<T> register = DeferredRegister.create(registryKey, namespace);
            this.registry = register.makeRegistry(() -> {
                PortRegistryMaker<T> maker = new PortRegistryMaker<>();
                consumer.accept(maker);
                return maker.make();
            });
            register.register(PortBus.MOD.unwrap());
        }

        public @Nullable ResourceLocation getKey(T value) {
            return registry.get().getKey(value);
        }

        public Optional<ResourceKey<T>> getResourceKey(T value) {
            return registry.get().getResourceKey(value);
        }

        public @Nullable T get(@Nullable ResourceKey<T> key) {
            return get(key.location());
        }

        public @Nullable T get(@Nullable ResourceLocation name) {
            return registry.get().getValue(name);
        }

        public boolean containsKey(ResourceLocation name) {
            return registry.get().containsKey(name);
        }

        public boolean containsKey(ResourceKey<T> key) {
            return containsKey(key.location());
        }

        public Optional<Holder.Reference<T>> getHolder(ResourceLocation location) {
            return registry.get().getDelegate(location);
        }

        public Optional<Holder.Reference<T>> getHolder(ResourceKey<T> key) {
            return registry.get().getDelegate(key);
        }
    }
}
