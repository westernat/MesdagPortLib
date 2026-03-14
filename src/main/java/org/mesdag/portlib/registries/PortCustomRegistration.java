package org.mesdag.portlib.registries;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.event.PortBus;
import org.mesdag.portlib.registries.callback.PortRegistryCallback;

import java.util.Optional;
import java.util.function.Consumer;

public class PortCustomRegistration<T> extends PortRegistration<T> {
    final PortRegistryMaker<T> maker;
    final Registry<T> registry;

    PortCustomRegistration(String namespace, ResourceKey<? extends Registry<T>> registryKey, Consumer<PortRegistryMaker<T>> consumer) {
        super(namespace, registryKey);
        DeferredRegister<T> register = DeferredRegister.create(registryKey, namespace);
        this.maker = new PortRegistryMaker<>();
        this.registry = register.makeRegistry(builder -> {
            maker.builder = builder;
            consumer.accept(maker);
        });
        register.register(PortBus.MOD.unwrap());
    }

    public void register(ResourceLocation key, T value) {
        Registry.register(registry, key, value);
    }

    public void addCallback(PortRegistryCallback<T> callback) {
        maker.callback(callback);
    }

    public @Nullable ResourceLocation getKey(T value) {
        return registry.getKey(value);
    }

    public Optional<ResourceKey<T>> getResourceKey(T value) {
        return registry.getResourceKey(value);
    }

    public @Nullable T get(@Nullable ResourceKey<T> key) {
        return registry.get(key);
    }

    public @Nullable T get(@Nullable ResourceLocation name) {
        return registry.get(name);
    }

    public boolean containsKey(ResourceLocation name) {
        return registry.containsKey(name);
    }

    public boolean containsKey(ResourceKey<T> key) {
        return registry.containsKey(key);
    }

    public Optional<Holder.Reference<T>> getHolder(ResourceLocation location) {
        return registry.getHolder(location);
    }

    public Optional<Holder.Reference<T>> getHolder(ResourceKey<T> key) {
        return registry.getHolder(key);
    }

    public boolean containsValue(T value) {
        return registry.containsValue(value);
    }
}
