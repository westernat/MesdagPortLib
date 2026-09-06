package org.mesdag.portlib.wrapper.world.entity.ai.attributes;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.registries.ForgeRegistries;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.core.PortHolder;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class AttributeHolder implements PortHolder<Attribute> {
    private Attribute value;
    private final Holder<Attribute> delegate;

    @Diff
    public AttributeHolder(Attribute value) {
        this.value = value;
        this.delegate = PortHolder.getDelegate(ForgeRegistries.ATTRIBUTES, value);
    }

    private AttributeHolder(Holder<Attribute> delegate) {
        this.delegate = delegate;
    }

    @Diff
    public static AttributeHolder wrap(Holder<Attribute> delegate) {
        return new AttributeHolder(delegate);
    }

    @Diff
    public static AttributeHolder wrap(Attribute delegate) {
        return new AttributeHolder(delegate);
    }

    @Diff
    public static AttributeHolder lazy(Supplier<Holder<Attribute>> sup, boolean memoize) {
        Supplier<Holder<Attribute>> mem = memoize ? Suppliers.memoize(sup) : sup;
        return wrap(new Holder<>() {
            @Override
            public Attribute value() {
                return mem.get().get();
            }

            @Override
            public boolean isBound() {
                return mem.get().isBound();
            }

            @Override
            public boolean is(ResourceLocation location) {
                return mem.get().is(location);
            }

            @Override
            public boolean is(ResourceKey<Attribute> resourceKey) {
                return mem.get().is(resourceKey);
            }

            @Override
            public boolean is(Predicate<ResourceKey<Attribute>> predicate) {
                return mem.get().is(predicate);
            }

            @Override
            public boolean is(TagKey<Attribute> tagKey) {
                return mem.get().is(tagKey);
            }

            @Override
            public Stream<TagKey<Attribute>> tags() {
                return mem.get().tags();
            }

            @Override
            public Either<ResourceKey<Attribute>, Attribute> unwrap() {
                return mem.get().unwrap();
            }

            @Override
            public Optional<ResourceKey<Attribute>> unwrapKey() {
                return mem.get().unwrapKey();
            }

            @Override
            public Kind kind() {
                return mem.get().kind();
            }

            @Override
            public boolean canSerializeIn(HolderOwner<Attribute> owner) {
                return mem.get().canSerializeIn(owner);
            }
        });
    }

    @Override
    public Holder<Attribute> delegate() {
        return delegate;
    }

    @Override
    public Attribute value() {
        return value == null ? delegate.value() : value;
    }
}
