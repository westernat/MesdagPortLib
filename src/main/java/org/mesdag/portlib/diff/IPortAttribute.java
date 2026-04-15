package org.mesdag.portlib.diff;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.TooltipFlag;
import org.mesdag.portlib.wrapper.PortSelfGetter;
import org.mesdag.portlib.wrapper.common.PortPercentageAttribute;
import org.mesdag.portlib.wrapper.world.entity.ai.attributes.PortAttribute;
import org.mesdag.portlib.wrapper.world.entity.ai.attributes.PortAttributeModifier;

@Diff
public interface IPortAttribute extends PortSelfGetter<Attribute> {
    void portlib$setSentiment(PortAttribute.PortSentiment sentiment);

    PortAttribute.PortSentiment portlib$getSentiment();

    static IPortAttribute of(Attribute attribute) {
        return (IPortAttribute) attribute;
    }

    static <E> E fromElement(E e, Attribute a, AttributeModifier m, TooltipFlag f, boolean p) {
        MutableComponent component;
        if (a instanceof PortPercentageAttribute attribute) {
            component = attribute.toValueComponent(PortAttributeModifier.PortOperation.wrap(m.getOperation()), m.getAmount(), f);
        } else if (e instanceof MutableComponent c) {
            component = c;
        } else if (e instanceof Component c) {
            component = c.copy();
        } else {
            return e;
        }
        PortAttribute.PortSentiment sentiment = of(a).portlib$getSentiment();
        if (sentiment != PortAttribute.PortSentiment.POSITIVE) {
            component.withStyle(sentiment.getStyle(p));
        }
        return (E) component;
    }
}
