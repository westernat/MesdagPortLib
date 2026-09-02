package org.mesdag.portlib.wrapper.common.extensions;

import net.minecraft.core.IdMap;

@SuppressWarnings("all")
public interface IPortIdMapExtension<T> {
    private IdMap<T> self() {
        return (IdMap<T>) this;
    }

    default int getIdOrThrow(T value) {
        int i = self().getId(value);
        if (i == -1) {
            throw new IllegalArgumentException("Can't find id for '" + value + "' in map " + self());
        }
        return i;
    }

    static <T> IPortIdMapExtension<T> of(IdMap<T> idMap) {
        return (IPortIdMapExtension<T>) idMap;
    }
}
