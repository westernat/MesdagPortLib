package org.mesdag.portlib.datamap;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.registries.datamaps.DataMapValueRemover;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.core.PortRegistry;

import java.util.Optional;

@FunctionalInterface
public interface PortDataMapValueRemover<R, T> {
    Optional<T> remove(T value, PortRegistry<R> registry, Either<TagKey<R>, ResourceKey<R>> source, R object);

    @Diff
    default DataMapValueRemover<R, T> unwrap() {
        return (value, registry, source, object) -> remove(value, PortRegistry.wrap(registry), source, object);
    }

    @Diff
    static <R, T> PortDataMapValueRemover<R, T> wrap(DataMapValueRemover<R, T> delegate) {
        return new Delegate<>(delegate);
    }

    @Diff
    record Delegate<R, T>(DataMapValueRemover<R, T> delegate) implements PortDataMapValueRemover<R, T> {
        @Override
        public Optional<T> remove(T value, PortRegistry<R> registry, Either<TagKey<R>, ResourceKey<R>> source, R object) {
            return delegate.remove(value, registry.unwrap(), source, object);
        }

        @Override
        public DataMapValueRemover<R, T> unwrap() {
            return delegate;
        }
    }

    class PortDefault<T, R> implements PortDataMapValueRemover<R, T> {
        public static final PortDefault<?, ?> INSTANCE = new PortDefault<>();

        public static <T, R> PortDefault<T, R> defaultRemover() {
            return (PortDefault<T, R>) INSTANCE;
        }

        public static <T, R> Codec<PortDefault<T, R>> codec() {
            return Codec.unit(defaultRemover());
        }

        private PortDefault() {}

        @Override
        public Optional<T> remove(T value, PortRegistry<R> registry, Either<TagKey<R>, ResourceKey<R>> source, R object) {
            return Optional.empty();
        }
    }
}
