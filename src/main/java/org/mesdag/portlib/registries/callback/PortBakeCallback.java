package org.mesdag.portlib.registries.callback;

import org.mesdag.portlib.wrapper.core.PortRegistry;

@FunctionalInterface
public non-sealed interface PortBakeCallback<V> extends PortRegistryCallback<V> {
    void onBake(PortRegistry<V> owner);
}
