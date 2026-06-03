package PortLib.extensions.net.minecraft.core.IdMap;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.minecraft.core.IdMap;

@Extension
public class PortIdMapExtension {
    public static <T> int getIdOrThrow(@This IdMap<T> thiz, T value) {
        int i = thiz.getId(value);
        if (i == -1) {
            throw new IllegalArgumentException("Can't find id for '" + value + "' in map " + thiz);
        }
        return i;
    }
}
