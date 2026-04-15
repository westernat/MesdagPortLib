package org.mesdag.portlib.wrapper.common;

import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.item.TooltipFlag;
import org.mesdag.portlib.wrapper.world.entity.ai.attributes.PortAttributeModifier;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class PortPercentageAttribute extends RangedAttribute {
    public static final DecimalFormat FORMAT = Util.make(new DecimalFormat("#.##"), fmt -> fmt.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ROOT)));
    protected final double scaleFactor;

    public PortPercentageAttribute(String descriptionId, double defaultValue, double min, double max, double scaleFactor) {
        super(descriptionId, defaultValue, min, max);
        this.scaleFactor = scaleFactor;
    }

    public PortPercentageAttribute(String descriptionId, double defaultValue, double min, double max) {
        this(descriptionId, defaultValue, min, max, 100);
    }

    public MutableComponent toValueComponent(PortAttributeModifier.PortOperation op, double value, TooltipFlag flag) {
        if (op == null || op == PortAttributeModifier.PortOperation.ADD_VALUE) {
            return Component.translatable("portlib.value.percent", FORMAT.format(value * this.scaleFactor));
        }

        return Component.translatable("portlib.value.percent", FORMAT.format(value * 100));
    }
}
