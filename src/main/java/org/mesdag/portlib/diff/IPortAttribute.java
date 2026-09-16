package org.mesdag.portlib.diff;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;
import org.mesdag.portlib.wrapper.PortSelfGetter;
import org.mesdag.portlib.wrapper.common.extensions.IPortAttributeExtension;
import org.mesdag.portlib.wrapper.common.extensions.IPortAttributeModifierExtension;

@Diff
public interface IPortAttribute extends PortSelfGetter<Attribute>, IPortAttributeExtension {
    void portlib$setSentiment(Sentiment sentiment);

    Sentiment portlib$getSentiment();

    static @NotNull IPortAttribute of(Attribute attribute) {
        return (IPortAttribute) attribute;
    }

    @SuppressWarnings("unchecked")
    static <E> E fromElement(E element, Attribute attribute, AttributeModifier modifier, TooltipFlag flag) {
        MutableComponent component;
        if (attribute instanceof IPortAttributeExtension extension) {
            component = extension.toComponent(IPortAttributeModifierExtension.of(modifier).wrap(), flag);
        } else if (element instanceof MutableComponent c) {
            component = c;
        } else if (element instanceof Component c) {
            component = c.copy();
        } else {
            return element;
        }
        Sentiment sentiment = of(attribute).portlib$getSentiment();
        if (sentiment != IPortAttributeExtension.Sentiment.POSITIVE) {
            component.withStyle(sentiment.getStyle(modifier.getAmount() > 0));
        }
        return (E) component;
    }
}
