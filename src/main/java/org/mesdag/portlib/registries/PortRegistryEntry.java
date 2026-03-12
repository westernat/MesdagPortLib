package org.mesdag.portlib.registries;

import com.google.common.base.Supplier;
import net.minecraft.core.Registry;
import net.minecraftforge.registries.RegistryObject;
import org.mesdag.portlib.wrapper.PortIdentifier;

@SuppressWarnings("all")
public final class PortRegistryEntry<T, R extends Registry<T>> implements Supplier<T> {
    final PortIdentifier identifier;
    RegistryObject<T> object;
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
