package org.mesdag.portlib.registries;

import com.google.common.base.Supplier;
import net.minecraft.core.Holder;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.PortIdentifier;

@SuppressWarnings("all")
public final class PortRegistryEntry<T> implements Supplier<T> {
    final PortIdentifier identifier;
    DeferredHolder<T, ? extends T> object;
    final Supplier<T> valueSupplier;

    private Object raw;

    public PortRegistryEntry(PortIdentifier identifier, Supplier<T> valueSupplier) {
        this.identifier = identifier;
        this.valueSupplier = valueSupplier;
    }

    @Diff
    public void setRaw(Object raw) {
        this.raw = raw;
    }

    @Diff
    public Object getRaw() {
        return this.raw;
    }

    @Diff
    public Holder<T> asHolder() {
        return (Holder<T>) this.object;
    }

    @Override
    public T get() {
        if (object == null) {
            throw new IllegalStateException("Registry Object not yet initialized for: " + identifier);
        }
        return object.get();
    }

    public PortIdentifier getId() {
        return identifier;
    }
}