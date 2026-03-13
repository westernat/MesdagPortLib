package org.mesdag.portlib.wrapper.core;

import net.minecraft.core.IdMap;

public class PortIdMap {
    public static <T> int getIdOrThrow(IdMap<T> idMap, T value) {
        int i = idMap.getId(value);
        if (i == -1) {
            throw new IllegalArgumentException("Can't find id for '" + value + "' in map " + idMap);
        } else {
            return i;
        }
    }
}
