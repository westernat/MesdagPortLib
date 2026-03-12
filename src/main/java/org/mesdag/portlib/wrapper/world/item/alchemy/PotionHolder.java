package org.mesdag.portlib.wrapper.world.item.alchemy;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.alchemy.Potion;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.core.PortHolder;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

@SuppressWarnings("all")
public class PotionHolder implements PortHolder<Potion> {
    private final Holder<Potion> delegate;

    private PotionHolder(Holder<Potion> holder) {
        this.delegate = holder;
    }

    @Diff
    public static PotionHolder wrap(Holder<Potion> holder) {
        return new PotionHolder(holder);
    }

    @Override
    public Potion value() {
        return delegate.value();
    }

    @Override
    public boolean isBound() {
        return delegate.isBound();
    }

    @Override
    public boolean is(ResourceLocation location) {
        return delegate.is(location);
    }

    @Override
    public boolean is(ResourceKey<Potion> resourceKey) {
        return delegate.is(resourceKey);
    }

    @Override
    public boolean is(Predicate<ResourceKey<Potion>> predicate) {
        return delegate.is(predicate);
    }

    @Override
    public boolean is(TagKey<Potion> tagKey) {
        return delegate.is(tagKey);
    }

    @Override
    public boolean is(Holder<Potion> holder) {
        return delegate.is(holder);
    }

    @Override
    public Stream<TagKey<Potion>> tags() {
        return delegate.tags();
    }

    @Override
    public Either<ResourceKey<Potion>, Potion> unwrap() {
        return delegate.unwrap();
    }

    @Override
    public Optional<ResourceKey<Potion>> unwrapKey() {
        return delegate.unwrapKey();
    }

    @Override
    public Kind kind() {
        return delegate.kind();
    }

    @Override
    public boolean canSerializeIn(HolderOwner<Potion> owner) {
        return delegate.canSerializeIn(owner);
    }
}
