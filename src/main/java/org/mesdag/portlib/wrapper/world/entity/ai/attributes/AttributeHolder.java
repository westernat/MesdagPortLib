package org.mesdag.portlib.wrapper.world.entity.ai.attributes;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.registries.ForgeRegistries;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.core.PortHolder;

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

    @Override
    public Holder<Attribute> delegate() {
        return delegate;
    }

    @Override
    public Attribute value() {
        return value == null ? delegate.value() : value;
    }
}
