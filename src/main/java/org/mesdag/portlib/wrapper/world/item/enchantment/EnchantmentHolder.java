package org.mesdag.portlib.wrapper.world.item.enchantment;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.enchantment.Enchantment;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.core.PortHolder;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

@SuppressWarnings("all")
public class EnchantmentHolder implements PortHolder<Enchantment> {
    private final Holder<Enchantment> delegate;

    private EnchantmentHolder(Holder<Enchantment> value) {
        this.delegate = value;
    }

    @Diff
    public static EnchantmentHolder wrap(Holder<Enchantment> value) {
        return new EnchantmentHolder(value);
    }

    @Override
    public Enchantment value() {
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
    public boolean is(ResourceKey<Enchantment> resourceKey) {
        return delegate.is(resourceKey);
    }

    @Override
    public boolean is(Predicate<ResourceKey<Enchantment>> predicate) {
        return delegate.is(predicate);
    }

    @Override
    public boolean is(TagKey<Enchantment> tagKey) {
        return delegate.is(tagKey);
    }

    @Override
    public boolean is(Holder<Enchantment> holder) {
        return delegate.is(holder);
    }

    @Override
    public Stream<TagKey<Enchantment>> tags() {
        return delegate.tags();
    }

    @Override
    public Either<ResourceKey<Enchantment>, Enchantment> unwrap() {
        return delegate.unwrap();
    }

    @Override
    public Optional<ResourceKey<Enchantment>> unwrapKey() {
        return delegate.unwrapKey();
    }

    @Override
    public Kind kind() {
        return delegate.kind();
    }

    @Override
    public boolean canSerializeIn(HolderOwner<Enchantment> owner) {
        return delegate.canSerializeIn(owner);
    }
}
