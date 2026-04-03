package org.mesdag.portlib.wrapper.world.entity.ai.attributes;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.core.PortHolder;

public class AttributeHolder implements PortHolder<Attribute> {
    private final Holder<Attribute> delegate;

    private AttributeHolder(Holder<Attribute> delegate) {
        this.delegate = delegate;
    }

    @Diff
    public static AttributeHolder wrap(Holder<Attribute> delegate) {
        return new AttributeHolder(delegate);
    }

    @Override
    public Holder<Attribute> delegate() {
        return delegate;
    }
}
