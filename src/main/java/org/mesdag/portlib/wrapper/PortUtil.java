package org.mesdag.portlib.wrapper;

import com.google.common.collect.ImmutableList;

import java.util.List;

@SuppressWarnings("all")
public class PortUtil {
    public static <T> List<T> copyAndAdd(List<T> list, T value) {
        return ImmutableList.<T>builderWithExpectedSize(list.size() + 1).addAll(list).add(value).build();
    }

    public static <T> List<T> copyAndAdd(T value, List<T> list) {
        return ImmutableList.<T>builderWithExpectedSize(list.size() + 1).add(value).addAll(list).build();
    }
}
