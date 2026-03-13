package org.mesdag.portlib.registries.callback;

import org.mesdag.portlib.wrapper.core.PortRegistry;

@FunctionalInterface
public non-sealed interface PortClearCallback<V> extends PortRegistryCallback<V> {
    void onClear(PortRegistry<V> owner);
}
