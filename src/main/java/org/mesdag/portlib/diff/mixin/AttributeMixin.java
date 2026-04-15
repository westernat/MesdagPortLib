package org.mesdag.portlib.diff.mixin;

import net.minecraft.world.entity.ai.attributes.Attribute;
import org.mesdag.portlib.diff.IPortAttribute;
import org.mesdag.portlib.wrapper.world.entity.ai.attributes.PortAttribute;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Attribute.class)
public abstract class AttributeMixin implements IPortAttribute {
    @Unique
    private PortAttribute.PortSentiment portlib$sentiment = PortAttribute.PortSentiment.POSITIVE;

    @Override
    public void portlib$setSentiment(PortAttribute.PortSentiment sentiment) {
        this.portlib$sentiment = sentiment;
    }

    @Override
    public PortAttribute.PortSentiment portlib$getSentiment() {
        return portlib$sentiment;
    }
}
