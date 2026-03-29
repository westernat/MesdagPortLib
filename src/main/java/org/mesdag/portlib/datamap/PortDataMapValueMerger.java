package org.mesdag.portlib.datamap;

import com.mojang.datafixers.util.Either;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.registries.datamaps.DataMapValueMerger;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.core.PortRegistry;

import java.util.*;

@FunctionalInterface
public interface PortDataMapValueMerger<R, T> {
    T merge(PortRegistry<R> registry, Either<TagKey<R>, ResourceKey<R>> first, T firstValue, Either<TagKey<R>, ResourceKey<R>> second, T secondValue);

    @Diff
    default DataMapValueMerger<R, T> unwrap() {
        return (registry, first, firstValue, second, secondValue) -> {
            PortRegistry<R> wrapped = PortRegistry.wrap(registry);
            return merge(wrapped, first, firstValue, second, secondValue);
        };
    }

    @Diff
    static <R, T> PortDataMapValueMerger<R, T> wrap(DataMapValueMerger<R, T> delegate) {
        return new Delegate<>(delegate);
    }

    @Diff
    record Delegate<R, T>(DataMapValueMerger<R, T> delegate) implements PortDataMapValueMerger<R, T> {

        @Override
        public T merge(PortRegistry<R> registry, Either<TagKey<R>, ResourceKey<R>> first, T firstValue, Either<TagKey<R>, ResourceKey<R>> second, T secondValue) {
            return delegate.merge(registry.unwrap(), first, firstValue, second, secondValue);
        }

        @Override
        public DataMapValueMerger<R, T> unwrap() {
            return delegate;
        }
    }

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
