package org.mesdag.portlib.registries.callback;

import net.minecraft.resources.ResourceKey;
import org.mesdag.portlib.wrapper.core.PortRegistry;

@FunctionalInterface
public non-sealed interface PortAddCallback<V> extends PortRegistryCallback<V> {
    void onAdd(PortRegistry<V> owner, int id, ResourceKey<V> key, V value);
}
