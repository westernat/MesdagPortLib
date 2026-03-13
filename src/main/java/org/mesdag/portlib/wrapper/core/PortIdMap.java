package org.mesdag.portlib.wrapper.core;

import net.minecraft.core.IdMap;

public class PortIdMap {
    public static <T> int getIdOrThrow(IdMap<T> idMap, T value) {
        return idMap.getIdOrThrow(value);
    }
}
