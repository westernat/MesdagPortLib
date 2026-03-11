package org.mesdag.portlib.register;

import com.google.common.base.Supplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.mesdag.portlib.wrapper.PortIdentifier;

@SuppressWarnings("all")
public final class PortRegistryEntry<T, R extends Registry<T>> implements Supplier<T> {
    final ResourceKey<R> registryKey;
    final PortIdentifier identifier;
    private final DeferredHolder<T, ? extends T> object;
    final Supplier<T> valueSupplier;

    public PortRegistryEntry(ResourceKey<R> registryKey, PortIdentifier identifier, Supplier<T> valueSupplier) {
        this.registryKey = registryKey;
        this.identifier = identifier;
        this.object = DeferredHolder.create(registryKey, identifier);
        this.valueSupplier = valueSupplier;
    }

    @Override
    public T get() {
        return object.get();
    }

    public PortIdentifier getId() {
        return identifier;
    }
}
