package org.mesdag.portlib.wrapper;

import net.minecraft.Util;

import java.util.List;

@SuppressWarnings("all")
public class PortUtil {
    public static <T> List<T> copyAndAdd(List<T> list, T value) {
        return Util.copyAndAdd(list, value);
    }

    public static <T> List<T> copyAndAdd(T value, List<T> list) {
        return Util.copyAndAdd(value, list);
    }
}
