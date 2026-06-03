package org.mesdag.portlib.wrapper.common.extensions;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.wrapper.world.entity.ai.attributes.PortAttributeModifier;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public interface IPortAttributeExtension {
    DecimalFormat FORMAT = Util.make(new DecimalFormat("#.##"), fmt -> fmt.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ROOT)));

    private Attribute self() {
        return (Attribute) this;
    }

    default MutableComponent toValueComponent(@Nullable PortAttributeModifier.PortOperation op, double value, TooltipFlag flag) {
        if (isNullOrAddition(op)) {
            return Component.translatable("portlib.value.flat", FORMAT.format(value));
        }

        return Component.translatable("portlib.value.percent", FORMAT.format(value * 100));
    }

    default MutableComponent toComponent(PortAttributeModifier modif, TooltipFlag flag) {
        Attribute attr = self();
        double value = modif.amount();
        String key = value > 0 ? "portlib.modifier.plus" : "portlib.modifier.take";
        ChatFormatting color = attr.getStyle(value > 0);

        Component attrDesc = Component.translatable(attr.getDescriptionId());
        Component valueComp = toValueComponent(modif.operation(), value, flag);

        return Component.translatable(key, valueComp, attrDesc).withStyle(color);
    }

    static boolean isNullOrAddition(@Nullable PortAttributeModifier.PortOperation op) {
        return op == null || op == PortAttributeModifier.PortOperation.ADD_VALUE;
    }
}
