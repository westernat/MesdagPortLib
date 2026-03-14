package org.mesdag.portlib.registries.callback;

import net.neoforged.neoforge.registries.callback.RegistryCallback;
import org.mesdag.portlib.diff.Diff;

public sealed interface PortRegistryCallback<T> permits PortAddCallback, PortBakeCallback, PortClearCallback {
    @Diff
    RegistryCallback<T> unwrap();
}
