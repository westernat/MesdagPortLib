package org.mesdag.portlib.diff.mixin;

import net.minecraft.world.entity.ai.attributes.Attribute;
import org.mesdag.portlib.diff.IPortAttribute;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Attribute.class)
public abstract class AttributeMixin implements IPortAttribute {
    @Unique
    private Sentiment portlib$sentiment = Sentiment.POSITIVE;

    @Override
    public void portlib$setSentiment(Sentiment sentiment) {
        this.portlib$sentiment = sentiment;
    }

    @Override
    public Sentiment portlib$getSentiment() {
        return portlib$sentiment;
    }
}
