package org.mesdag.portlib.wrapper.core;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraftforge.registries.IForgeRegistry;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface PortHolder<T> extends Holder<T> {
    @Diff
    static <T> Holder<T> getDelegate(IForgeRegistry<T> registry, T value) {
        Optional<Holder<T>> optional = registry.getHolder(value);
        if (optional.isEmpty()) {
            throw new IllegalArgumentException("Not registered: " + value);
        }
        return optional.get();
    }

    @Diff
    static <T> @Nullable ResourceKey<T> getKey(Holder<T> holder) {
        return holder.unwrapKey().orElse(null);
    }

    Holder<T> delegate();

    @Override
    default T value() {
        return delegate().value();
    }

    @Override
    default boolean isBound() {
        return delegate().isBound();
    }

    @Override
    default boolean is(ResourceLocation location) {
        return delegate().is(location);
    }

    @Override
    default boolean is(ResourceKey<T> resourceKey) {
        return delegate().is(resourceKey);
    }

    @Override
    default boolean is(Predicate<ResourceKey<T>> predicate) {
        return delegate().is(predicate);
    }

    @Override
    default boolean is(TagKey<T> tagKey) {
        return delegate().is(tagKey);
    }

    @Override
    default Stream<TagKey<T>> tags() {
        return delegate().tags();
    }

    @Override
    default Either<ResourceKey<T>, T> unwrap() {
        return delegate().unwrap();
    }

    @Override
    default Optional<ResourceKey<T>> unwrapKey() {
        return delegate().unwrapKey();
    }

    @Override
    default Kind kind() {
        return delegate().kind();
    }

    @Override
    default boolean canSerializeIn(HolderOwner<T> owner) {
        return delegate().canSerializeIn(owner);
    }
}
