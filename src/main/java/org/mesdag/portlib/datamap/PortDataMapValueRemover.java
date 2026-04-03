package org.mesdag.portlib.datamap;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import org.mesdag.portlib.wrapper.core.PortRegistry;

import java.util.Optional;

@FunctionalInterface
public interface PortDataMapValueRemover<R, T> {
    Optional<T> remove(T value, PortRegistry<R> registry, Either<TagKey<R>, ResourceKey<R>> source, R object);

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
