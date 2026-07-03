package org.mesdag.portlib.wrapper.common;

import PortLib.extensions.net.minecraft.world.entity.ai.attributes.Attribute.PortAttributeExtension;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.wrapper.common.extensions.IPortAttributeExtension;
import org.mesdag.portlib.wrapper.world.entity.ai.attributes.PortAttributeModifier;

public class PortBooleanAttribute extends Attribute implements IPortAttributeExtension {
    public PortBooleanAttribute(String descriptionId, boolean defaultValue) {
        super(descriptionId, defaultValue ? 1 : 0);
    }

    @Override
    public double sanitizeValue(double value) {
        if (Double.isNaN(value)) {
            return 0;
        }
        return value > 0 ? 1 : 0;
    }

    @Override
    public MutableComponent toValueComponent(@Nullable PortAttributeModifier.Operation op, double value, TooltipFlag flag) {
        if (op == null) {
            return Component.translatable("portlib.value.boolean." + (value > 0 ? "enabled" : "disabled"));
        } else if (op == PortAttributeModifier.Operation.ADD_VALUE && value > 0) {
            return Component.translatable("portlib.value.boolean.enable");
        } else if (op == PortAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL && (int) value == -1) {
            return Component.translatable("portlib.value.boolean.disable");
        }
        return Component.translatable("portlib.value.boolean.invalid");
    }

    @Override
    public MutableComponent toComponent(PortAttributeModifier modif, TooltipFlag flag) {
        double value = modif.amount();

        ChatFormatting color = PortAttributeExtension.getStyle(this, value > 0);

        return Component.translatable("portlib.modifier.bool", toValueComponent(modif.operation(), value, flag), Component.translatable(getDescriptionId())).withStyle(color);
    }
}
