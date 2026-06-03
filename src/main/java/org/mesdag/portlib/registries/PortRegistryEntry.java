package org.mesdag.portlib.registries;

import com.google.common.base.Supplier;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraftforge.registries.RegistryObject;
import org.mesdag.portlib.diff.Diff;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class PortRegistryEntry<R, T extends R> implements Holder<R> {
    final ResourceLocation identifier;
    RegistryObject<T> object;
    Supplier<T> valueSupplier;

    public PortRegistryEntry(ResourceLocation identifier, Supplier<T> valueSupplier) {
        this.identifier = identifier;
        this.valueSupplier = valueSupplier;
    }

    @Diff
    public static <R, T extends R> PortRegistryEntry<R, T> wrap(ResourceLocation identifier, RegistryObject<T> object) {
        PortRegistryEntry<R, T> entry = new PortRegistryEntry<>(identifier, object::get);
        entry.object = object;
        return entry;
    }

    public Holder<R> asHolder() {
        return (Holder<R>) object.getHolder().orElseGet(() -> Holder.direct(get()));
    }

    @Override
    public T get() {
        return object.get();
    }

    public ResourceLocation getId() {
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
    public Holder.Kind kind() {
        return asHolder().kind();
    }

    @Override
    public boolean canSerializeIn(HolderOwner<R> owner) {
        return asHolder().canSerializeIn(owner);
    }

    // endregion Holder

    static class Memoized<R, T extends R> extends PortRegistryEntry<R, T> {
        public Memoized(String namespace, String name, Supplier<T> valueSupplier) {
            super(ResourceLocation.fromNamespaceAndPath(namespace, name), valueSupplier);
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
