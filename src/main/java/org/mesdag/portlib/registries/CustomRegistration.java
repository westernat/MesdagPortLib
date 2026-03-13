package org.mesdag.portlib.registries;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.event.PortBus;
import org.mesdag.portlib.registries.callback.PortRegistryCallback;

import java.util.Optional;
import java.util.function.Consumer;

public class CustomRegistration<T> extends Registration<T> {
    final PortRegistryMaker<T> maker;
    final java.util.function.Supplier<IForgeRegistry<T>> registry;

    CustomRegistration(String namespace, ResourceKey<? extends Registry<T>> registryKey, Consumer<PortRegistryMaker<T>> consumer) {
        super(namespace, registryKey);
        DeferredRegister<T> register = DeferredRegister.create(registryKey, namespace);
        this.maker = new PortRegistryMaker<>();
        this.registry = register.makeRegistry(() -> {
            consumer.accept(maker);
            return maker.make();
        });
        register.register(PortBus.MOD.unwrap());
    }

    public void addCallback(PortRegistryCallback<T> callback) {
        maker.callback(callback);
    }

    public void register(ResourceLocation key, T value) {
        registry.get().register(key, value);
    }

    public @Nullable ResourceLocation getKey(T value) {
        return registry.get().getKey(value);
    }

    public Optional<ResourceKey<T>> getResourceKey(T value) {
        return registry.get().getResourceKey(value);
    }

    public @Nullable T get(@Nullable ResourceKey<T> key) {
        if (key == null) return null;
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

    public boolean containsValue(T value) {
        return registry.get().containsValue(value);
    }
}
