package org.mesdag.portlib.wrapper.common;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.item.TooltipFlag;
import org.mesdag.portlib.wrapper.common.extensions.IPortAttributeExtension;
import org.mesdag.portlib.wrapper.world.entity.ai.attributes.PortAttributeModifier;

public class PortPercentageAttribute extends RangedAttribute implements IPortAttributeExtension {
    protected final double scaleFactor;

    public PortPercentageAttribute(String descriptionId, double defaultValue, double min, double max, double scaleFactor) {
        super(descriptionId, defaultValue, min, max);
        this.scaleFactor = scaleFactor;
    }

    public PortPercentageAttribute(String descriptionId, double defaultValue, double min, double max) {
        this(descriptionId, defaultValue, min, max, 100);
    }

    @Override
    public MutableComponent toValueComponent(PortAttributeModifier.PortOperation op, double value, TooltipFlag flag) {
        if (IPortAttributeExtension.isNullOrAddition(op)) {
            return Component.translatable("portlib.value.percent", FORMAT.format(value * this.scaleFactor));
        }

        return Component.translatable("portlib.value.percent", FORMAT.format(value * 100));
    }
}
