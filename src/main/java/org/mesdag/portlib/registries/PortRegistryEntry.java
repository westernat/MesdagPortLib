package org.mesdag.portlib.registries;

import com.google.common.base.Supplier;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.mesdag.portlib.wrapper.resources.PortIdentifier;

@SuppressWarnings("all")
public final class PortRegistryEntry<T> implements Supplier<T> {
    final PortIdentifier identifier;
    DeferredHolder<T, ? extends T> object;
    final Supplier<T> valueSupplier;

    public PortRegistryEntry(PortIdentifier identifier, Supplier<T> valueSupplier) {
        this.identifier = identifier;
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
