package org.mesdag.portlib.datamap;

import com.mojang.datafixers.util.Either;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import org.mesdag.portlib.wrapper.core.PortRegistry;

import java.util.*;

@FunctionalInterface
public interface PortDataMapValueMerger<R, T> {
    T merge(PortRegistry<R> registry, Either<TagKey<R>, ResourceKey<R>> first, T firstValue, Either<TagKey<R>, ResourceKey<R>> second, T secondValue);

    static <T, R> PortDataMapValueMerger<R, T> defaultMerger() {
        return (registry, first, firstValue, second, secondValue) -> secondValue;
    }

    static <T, R> PortDataMapValueMerger<R, List<T>> listMerger() {
        return (registry, first, firstValue, second, secondValue) -> {
            final List<T> list = new ArrayList<>(firstValue);
            list.addAll(secondValue);
            return list;
        };
    }

    static <T, R> PortDataMapValueMerger<R, Set<T>> setMerger() {
        return (registry, first, firstValue, second, secondValue) -> {
            final Set<T> set = new HashSet<>(firstValue);
            set.addAll(secondValue);
            return set;
        };
    }

    static <K, V, R> PortDataMapValueMerger<R, Map<K, V>> mapMerger() {
        return (registry, first, firstValue, second, secondValue) -> {
            final Map<K, V> map = new HashMap<>(firstValue);
            map.putAll(secondValue);
            return map;
        };
    }
}
