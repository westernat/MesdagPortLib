package org.mesdag.portlib.registries;

import PortLib.extensions.net.minecraft.core.Holder.PortHolderExtension;
import com.google.common.base.Supplier;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public class PortRegistryEntry<R, T extends R> implements Holder<R> {
    final ResourceLocation identifier;
    RegistryObject<T> object;
    Supplier<T> valueSupplier;

    public PortRegistryEntry(ResourceLocation identifier, @Nullable Supplier<T> valueSupplier) {
        this.identifier = identifier;
        this.valueSupplier = valueSupplier;
    }

    @Diff
    public PortRegistryEntry(RegistryObject<T> object) {
        this.identifier = object.getId();
        this.object = object;
    }

    @Diff
    public static <R, T extends R> PortRegistryEntry<R, T> wrap(ResourceLocation identifier, RegistryObject<T> object) {
        PortRegistryEntry<R, T> entry = new PortRegistryEntry<>(identifier, object::get);
        entry.object = object;
        return entry;
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
        return object.get();
    }

    @Override
    public boolean isBound() {
        return object.isPresent();
    }

    @Override
    public boolean is(ResourceLocation location) {
        return location.equals(getKey().location());
    }

    @Override
    public boolean is(ResourceKey<R> resourceKey) {
        return getKey() == resourceKey;
    }

    @Override
    public boolean is(Predicate<ResourceKey<R>> predicate) {
        return predicate.test(getKey());
    }

    @Override
    public boolean is(TagKey<R> tagKey) {
        return object.getHolder().map(holder -> ((Holder<R>) holder).is(tagKey)).orElse(false);
    }

    public boolean is(Holder<R> holder) {
        return object.getHolder().map(holder1 -> PortHolderExtension.is(((Holder<R>) holder1), holder)).orElse(false);
    }

    @Override
    public Stream<TagKey<R>> tags() {
        return object.getHolder().map(holder -> ((Holder<R>) holder).tags()).orElseGet(Stream::of);
    }

    @Override
    public Either<ResourceKey<R>, R> unwrap() {
        return Either.left(getKey());
    }

    @Override
    public Optional<ResourceKey<R>> unwrapKey() {
        return Optional.of(getKey());
    }

    @Override
    public Holder.Kind kind() {
        return Kind.REFERENCE;
    }

    @Override
    public boolean canSerializeIn(HolderOwner<R> owner) {
        return object.getHolder().map(holder -> ((Holder<R>) holder).canSerializeIn(owner)).orElse(false);
    }

    public ResourceKey<R> getKey() {
        return (ResourceKey<R>) object.getKey();
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
        public R value() {
            return valueSupplier.get();
        }
    }
}
