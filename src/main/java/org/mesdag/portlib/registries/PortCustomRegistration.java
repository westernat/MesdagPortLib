package org.mesdag.portlib.registries;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.event.PortBus;
import org.mesdag.portlib.registries.callback.PortRegistryCallback;

import java.util.Optional;
import java.util.function.Consumer;

@SuppressWarnings("UnstableApiUsage")
public class PortCustomRegistration<R> extends PortRegistration<R> {
    final PortRegistryMaker<R> maker;
    final java.util.function.Supplier<IForgeRegistry<R>> registry;

    PortCustomRegistration(String namespace, ResourceKey<? extends Registry<R>> registryKey, Consumer<PortRegistryMaker<R>> consumer) {
        super(namespace, registryKey, false);
        DeferredRegister<R> register = DeferredRegister.create(registryKey, namespace);
        this.maker = new PortRegistryMaker<>();
        this.registry = register.makeRegistry(() -> {
            consumer.accept(maker);
            return maker.make();
        });
        register.register(PortBus.MOD.unwrap(namespace));
    }

    public void addCallback(PortRegistryCallback<R> callback) {
        maker.callback(callback);
    }

    public void register(ResourceLocation key, R value) {
        if (registry.get() instanceof ForgeRegistry<R> registry1) {
            boolean locked = registry1.isLocked();
            if (locked) registry1.unfreeze();
            registry1.register(key, value);
            if (locked) registry1.freeze();
        }
    }

    public @Nullable ResourceLocation getKey(R value) {
        return registry.get().getKey(value);
    }

    public Optional<ResourceKey<R>> getResourceKey(R value) {
        return registry.get().getResourceKey(value);
    }

    public @Nullable R get(@Nullable ResourceKey<R> key) {
        if (key == null) return null;
        return get(key.location());
    }

    public @Nullable R get(@Nullable ResourceLocation name) {
        return registry.get().getValue(name);
    }

    public boolean containsKey(ResourceLocation name) {
        return registry.get().containsKey(name);
    }

    public boolean containsKey(ResourceKey<R> key) {
        return containsKey(key.location());
    }

    public Optional<Holder.Reference<R>> getHolder(ResourceLocation location) {
        return registry.get().getDelegate(location);
    }

    public Optional<Holder.Reference<R>> getHolder(ResourceKey<R> key) {
        return registry.get().getDelegate(key);
    }

    public boolean containsValue(R value) {
        return registry.get().containsValue(value);
    }

    public Codec<R> byNameCodec() {
        return ResourceLocation.CODEC.xmap(this::get, this::getKey);
    }
}
