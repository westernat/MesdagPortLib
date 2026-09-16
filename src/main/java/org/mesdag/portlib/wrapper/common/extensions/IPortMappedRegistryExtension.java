package org.mesdag.portlib.wrapper.common.extensions;

import org.mesdag.portlib.diff.IPortMappedRegistry;
import org.mesdag.portlib.registries.callback.PortAddCallback;

public interface IPortMappedRegistryExtension<T> {
    default void onAdd(PortAddCallback.Vanilla<T> callback) {
        ((IPortMappedRegistry<T>) this).confluence$onAdd(callback);
    }
}
