package org.mesdag.portlib.registries.callback;

public sealed interface PortRegistryCallback<T> permits PortAddCallback, PortBakeCallback, PortClearCallback {
}
