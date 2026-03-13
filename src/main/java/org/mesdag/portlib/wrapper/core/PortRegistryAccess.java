package org.mesdag.portlib.wrapper.core;

import com.mojang.serialization.DynamicOps;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import org.mesdag.portlib.wrapper.PortEnvironment;

import java.util.Optional;
import java.util.stream.Stream;

@SuppressWarnings("all")
public class PortRegistryAccess implements RegistryAccess {
    private final RegistryAccess delegate;

    public PortRegistryAccess() {
        this(PortEnvironment.registryAccess());
    }

    public PortRegistryAccess(RegistryAccess delegate) {
        this.delegate = delegate;
    }

    @Override
    public <E> Optional<Registry<E>> registry(ResourceKey<? extends Registry<? extends E>> registryKey) {
        return delegate.registry(registryKey);
    }

    @Override
    public Stream<RegistryEntry<?>> registries() {
        return delegate.registries();
    }

    public Stream<ResourceKey<? extends Registry<?>>> listRegistries() {
        return registries().map(RegistryAccess.RegistryEntry::key);
    }

    public <V> RegistryOps<V> createSerializationContext(DynamicOps<V> ops) {
        return RegistryOps.create(ops, this);
    }
}
