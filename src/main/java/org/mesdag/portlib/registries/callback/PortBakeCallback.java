package org.mesdag.portlib.registries.callback;

import net.neoforged.neoforge.registries.callback.BakeCallback;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.core.PortRegistry;

@FunctionalInterface
public non-sealed interface PortBakeCallback<V> extends PortRegistryCallback<V> {
    void onBake(PortRegistry<V> owner);

    @Diff
    @Override
    default BakeCallback<V> unwrap() {
        return registry -> onBake((PortRegistry<V>) registry.wrap());
    }
}
