package org.mesdag.portlib.util;

import java.util.Set;
import java.util.function.Function;

public class PortSets {

    /**
     * 创建一个只读的转换 Set。
     * * @param fromSet 底层原始集合
     *
     * @param toFunction 正向转换函数 (F -> T)
     * @return 不可修改的转换视图
     */
    public static <F, T> Set<T> transform(Set<F> fromSet, Function<? super F, ? extends T> toFunction) {
        return new ImmutableTransformSet<>(fromSet, toFunction);
    }

    /**
     * 创建一个支持修改（删除）的转换 Set。
     * 适用于 1.21.1 原生支持同步的场景。
     * * @param fromSet 底层原始集合
     *
     * @param toFunction   正向转换函数 (F -> T)
     * @param fromFunction 反向转换函数 (T -> F)，用于实现 O(1) 的 contains 和 remove
     * @return 可修改的转换视图
     */
    public static <F, T> Set<T> mutableTransform(Set<F> fromSet, Function<? super F, ? extends T> toFunction, Function<? super T, ? extends F> fromFunction) {
        return new MutableTransformSet<>(fromSet, toFunction, fromFunction, () -> {
        });
    }

    /**
     * 创建一个支持修改并带有副作用回调的转换 Set。
     * 专门用于 1.20.1 侧需要手动同步 NBT 的场景。
     * * @param fromSet 底层原始集合
     *
     * @param toFunction   正向转换函数 (F -> T)
     * @param fromFunction 反向转换函数 (T -> F)
     * @param onDirty      当集合发生改变（如执行了 remove）时的回调逻辑
     * @return 带同步功能的可修改转换视图
     */
    public static <F, T> Set<T> mutableTransform(Set<F> fromSet, Function<? super F, ? extends T> toFunction, Function<? super T, ? extends F> fromFunction, Runnable onDirty) {
        return new MutableTransformSet<>(fromSet, toFunction, fromFunction, onDirty);
    }
}