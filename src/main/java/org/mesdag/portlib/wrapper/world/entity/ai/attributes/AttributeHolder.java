package org.mesdag.portlib.wrapper.world.entity.ai.attributes;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.registries.ForgeRegistries;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.core.PortHolder;

public class AttributeHolder implements PortHolder<Attribute> {
    private final Attribute value;
    private final Holder<Attribute> delegate;

    private AttributeHolder(Attribute value) {
        this.value = value;
        this.delegate = PortHolder.getDelegate(ForgeRegistries.ATTRIBUTES, value);
    }

    @Diff
    public static AttributeHolder wrap(Attribute value) {
        return new AttributeHolder(value);
    }

    @Override
    public Holder<Attribute> delegate() {
        return delegate;
    }

    @Override
    public Attribute value() {
        return value;
    }
}
