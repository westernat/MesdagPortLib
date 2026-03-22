package org.mesdag.portlib.datamap;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import org.mesdag.portlib.wrapper.core.PortRegistry;

import java.util.Optional;

@FunctionalInterface
public interface DataMapValueRemover<R, T> {
    Optional<T> remove(T value, PortRegistry<R> registry, Either<TagKey<R>, ResourceKey<R>> source, R object);

    class Default<T, R> implements DataMapValueRemover<R, T> {
        public static final Default<?, ?> INSTANCE = new Default<>();

        public static <T, R> Default<T, R> defaultRemover() {
            return (Default<T, R>) INSTANCE;
        }

        public static <T, R> Codec<Default<T, R>> codec() {
            return Codec.unit(defaultRemover());
        }

        private Default() {}

        @Override
        public Optional<T> remove(T value, PortRegistry<R> registry, Either<TagKey<R>, ResourceKey<R>> source, R object) {
            return Optional.empty();
        }
    }
}
