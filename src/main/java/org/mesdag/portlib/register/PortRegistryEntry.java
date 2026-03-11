package org.mesdag.portlib.register;

import com.google.common.base.Supplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.registries.RegistryManager;
import net.minecraftforge.registries.RegistryObject;
import org.mesdag.portlib.wrapper.PortIdentifier;

@SuppressWarnings("all")
public final class PortRegistryEntry<T, R extends Registry<T>> implements Supplier<T> {
    final ResourceKey<R> registryKey;
    final PortIdentifier identifier;
    private final RegistryObject<T> object;
    final Supplier<T> valueSupplier;

    public PortRegistryEntry(ResourceKey<R> registryKey, PortIdentifier identifier, Supplier<T> valueSupplier) {
        this.registryKey = registryKey;
        this.identifier = identifier;
        this.object = RegistryObject.create(identifier, RegistryManager.ACTIVE.getRegistry(registryKey));
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
