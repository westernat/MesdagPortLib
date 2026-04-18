package org.mesdag.portlib.registries.callback;

import net.neoforged.neoforge.registries.callback.ClearCallback;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.core.PortRegistry;

@FunctionalInterface
public non-sealed interface PortClearCallback<V> extends PortRegistryCallback<V> {
    void onClear(PortRegistry<V> owner);

    @Diff
    @Override
    default ClearCallback<V> unwrap() {
        return (registry, full) -> onClear((PortRegistry<V>) registry.wrap());
    }
}
