package org.mesdag.portlib.registries;

import com.google.common.base.Supplier;
import net.minecraftforge.registries.RegistryObject;
import org.mesdag.portlib.wrapper.resources.PortIdentifier;

@SuppressWarnings("all")
public class PortRegistryEntry<T> implements Supplier<T> {
    final PortIdentifier identifier;
    RegistryObject<T> object;
    Supplier<T> valueSupplier;

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

    static class Memoized<T> extends PortRegistryEntry<T> {
        public Memoized(String namespace, String name, Supplier<T> valueSupplier) {
            super(PortIdentifier.fromNamespaceAndPath(namespace, name), valueSupplier);
        }

        @Override
        public T get() {
            return valueSupplier.get();
        }
    }
}
