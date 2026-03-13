package org.mesdag.portlib.registries;

import com.google.common.base.Supplier;
import net.minecraft.core.Holder;
import net.minecraftforge.registries.RegistryObject; // 1.20.1 使用这个
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.PortIdentifier;

@SuppressWarnings("all")
public final class PortRegistryEntry<T> implements Supplier<T> {
    final PortIdentifier identifier;
    RegistryObject<T> object;
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
        return this.object.getHolder().orElseThrow(() ->
            new IllegalStateException("Holder not available for: " + identifier));
    }

    @Override
    public T get() {
        return object.get();
    }

    public PortIdentifier getId() {
        return identifier;
    }
}