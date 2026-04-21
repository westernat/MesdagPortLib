package org.mesdag.portlib.registries;

import com.google.common.base.Supplier;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.mesdag.portlib.wrapper.resources.PortIdentifier;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class PortRegistryEntry<R, T extends R> implements Holder<R>, Supplier<T> {
    final PortIdentifier identifier;
    DeferredHolder<R, T> object;
    Supplier<T> valueSupplier;

    public PortRegistryEntry(PortIdentifier identifier, Supplier<T> valueSupplier) {
        this.identifier = identifier;
        this.valueSupplier = valueSupplier;
    }

    public Holder<R> asHolder() {
        return object;
    }

    @Override
    public T get() {
        return object.get();
    }

    public PortIdentifier getId() {
        return identifier;
    }

    // region Holder

    @Override
    public R value() {
        return asHolder().value();
    }

    @Override
    public boolean isBound() {
        return asHolder().isBound();
    }

    @Override
    public boolean is(ResourceLocation location) {
        return asHolder().is(location);
    }

    @Override
    public boolean is(ResourceKey<R> resourceKey) {
        return asHolder().is(resourceKey);
    }

    @Override
    public boolean is(Predicate<ResourceKey<R>> predicate) {
        return asHolder().is(predicate);
    }

    @Override
    public boolean is(TagKey<R> tagKey) {
        return asHolder().is(tagKey);
    }

    @Override
    public boolean is(Holder<R> holder) {
        return asHolder().is(holder);
    }

    @Override
    public Stream<TagKey<R>> tags() {
        return asHolder().tags();
    }

    @Override
    public Either<ResourceKey<R>, R> unwrap() {
        return asHolder().unwrap();
    }

    @Override
    public Optional<ResourceKey<R>> unwrapKey() {
        return asHolder().unwrapKey();
    }

    @Override
    public Kind kind() {
        return asHolder().kind();
    }

    @Override
    public boolean canSerializeIn(HolderOwner<R> owner) {
        return asHolder().canSerializeIn(owner);
    }

    // endregion Holder

    static class Memoized<R, T extends R> extends PortRegistryEntry<R, T> {
        public Memoized(String namespace, String name, Supplier<T> valueSupplier) {
            super(PortIdentifier.fromNamespaceAndPath(namespace, name), valueSupplier);
        }

        @Override
        public T get() {
            return valueSupplier.get();
        }

        @Override
        public Holder<R> asHolder() {
            return Holder.direct(get());
        }
    }
}
