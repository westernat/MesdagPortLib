package org.mesdag.portlib.util;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.function.Function;

public class PortLists {
    /// 创建一个只读的转换 List。
    ///
    /// @param fromList   底层原始列表
    /// @param toFunction 正向转换函数 (F -> T)
    /// @return 不可修改的转换视图
    public static <F, T> List<T> immutableTransform(List<F> fromList, Function<? super F, ? extends T> toFunction) {
        return Lists.transform(fromList, toFunction::apply);
    }

    /// 创建一个支持修改（删除）的转换 List。
    ///
    /// @param fromList     底层原始列表
    /// @param toFunction   正向转换函数 (F -> T)
    /// @param fromFunction 反向转换函数 (T -> F)，用于实现 O(1) 的 contains 和 remove
    /// @return 可修改的转换视图
    public static <F, T> List<T> mutableTransform(List<F> fromList, Function<? super F, ? extends T> toFunction, Function<? super T, ? extends F> fromFunction) {
        return new MutableTransformList<>(fromList, toFunction, fromFunction, () -> {});
    }

    /// 创建一个支持修改并带有副作用回调的转换 List。
    ///
    /// @param fromList     底层原始列表
    /// @param toFunction   正向转换函数 (F -> T)
    /// @param fromFunction 反向转换函数 (T -> F)
    /// @param onDirty      当列表发生改变（如执行了 remove）时的回调逻辑
    /// @return 带同步功能的可修改转换视图
    public static <F, T> List<T> mutableTransform(List<F> fromList, Function<? super F, ? extends T> toFunction, Function<? super T, ? extends F> fromFunction, Runnable onDirty) {
        return new MutableTransformList<>(fromList, toFunction, fromFunction, onDirty);
    }
}
