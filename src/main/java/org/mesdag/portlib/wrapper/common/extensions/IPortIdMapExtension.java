package org.mesdag.portlib.wrapper.common.extensions;

import PortLib.extensions.net.minecraft.core.IdMap.PortIdMapExtension;
import net.minecraft.core.IdMap;

@SuppressWarnings("all")
public interface IPortIdMapExtension<T> {

    private IdMap<T> self() {
        return (IdMap<T>) this;
    }

    default int getIdOrThrow(T value) {
        return PortIdMapExtension.getIdOrThrow(self(), value);
    }

    static <T> IPortIdMapExtension<T> of(IdMap<T> idMap) {
        return (IPortIdMapExtension<T>) idMap;
    }
}
