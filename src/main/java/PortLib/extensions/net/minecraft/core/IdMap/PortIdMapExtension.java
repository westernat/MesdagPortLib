package PortLib.extensions.net.minecraft.core.IdMap;

import net.minecraft.core.IdMap;

public class PortIdMapExtension {
    public static <T> int getIdOrThrow(IdMap<T> thiz, T value) {
        int i = thiz.getId(value);
        if (i == -1) {
            throw new IllegalArgumentException("Can't find id for '" + value + "' in map " + thiz);
        }
        return i;
    }
}
