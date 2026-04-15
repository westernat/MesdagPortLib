package org.mesdag.portlib.wrapper.common;

import net.minecraft.Util;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.common.PercentageAttribute;
import org.mesdag.portlib.wrapper.world.entity.ai.attributes.PortAttributeModifier;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class PortPercentageAttribute extends PercentageAttribute {
    public static final DecimalFormat FORMAT = Util.make(new DecimalFormat("#.##"), fmt -> fmt.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ROOT)));

    public PortPercentageAttribute(String descriptionId, double defaultValue, double min, double max, double scaleFactor) {
        super(descriptionId, defaultValue, min, max, scaleFactor);
    }

    public PortPercentageAttribute(String descriptionId, double defaultValue, double min, double max) {
        super(descriptionId, defaultValue, min, max);
    }

    public MutableComponent toValueComponent(PortAttributeModifier.PortOperation op, double value, TooltipFlag flag) {
        return toValueComponent(op.unwrap(), value, flag);
    }
}
