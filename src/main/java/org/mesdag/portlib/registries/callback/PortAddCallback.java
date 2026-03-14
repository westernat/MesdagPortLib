package org.mesdag.portlib.registries.callback;

import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.callback.AddCallback;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.core.PortRegistry;

@FunctionalInterface
public non-sealed interface PortAddCallback<V> extends PortRegistryCallback<V> {
    void onAdd(PortRegistry<V> owner, int id, ResourceKey<V> key, V value);

    @Diff
    @Override
    default AddCallback<V> unwrap() {
        return (registry, id, key, value) -> onAdd(PortRegistry.wrap(registry), id, key, value);
    }
}
