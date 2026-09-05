package org.mesdag.portlib.wrapper.common.extensions;

import com.mojang.serialization.Codec;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.IPortAttribute;
import org.mesdag.portlib.network.PortRegistryFriendlyByteBuf;
import org.mesdag.portlib.network.codec.PortByteBufCodecs;
import org.mesdag.portlib.network.codec.PortStreamCodec;
import org.mesdag.portlib.wrapper.world.entity.ai.attributes.AttributeHolder;
import org.mesdag.portlib.wrapper.world.entity.ai.attributes.PortAttributeModifier;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

@SuppressWarnings("all")
public interface IPortAttributeExtension {
    DecimalFormat FORMAT = Util.make(new DecimalFormat("#.##"), fmt -> fmt.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ROOT)));
    Codec<Attribute> DIRECT_CODEC = BuiltInRegistries.ATTRIBUTE.byNameCodec();
    Codec<Holder<Attribute>> CODEC = BuiltInRegistries.ATTRIBUTE.holderByNameCodec();
    PortStreamCodec<PortRegistryFriendlyByteBuf, Holder<Attribute>> STREAM_CODEC = PortByteBufCodecs.holderRegistry(Registries.ATTRIBUTE);
    PortStreamCodec<PortRegistryFriendlyByteBuf, Attribute> DIRECT_STREAM_CODEC = PortByteBufCodecs.registry(Registries.ATTRIBUTE);

    private Attribute self() {
        return (Attribute) this;
    }

    default ChatFormatting getStyle(boolean isPositive) {
        return IPortAttribute.of(self()).portlib$getSentiment().getStyle(isPositive);
    }

    default TextColor getMergedStyle(boolean isPositive) {
        TextColor mergedRed = TextColor.fromRgb(0xF93131);
        TextColor mergedBlue = TextColor.fromRgb(0x7A7AF9);
        TextColor mergedGray = TextColor.fromRgb(0xCCCCCC);
        return switch (IPortAttribute.of(self()).portlib$getSentiment()) {
            case POSITIVE -> isPositive ? mergedBlue : mergedRed;
            case NEGATIVE -> isPositive ? mergedRed : mergedBlue;
            case NEUTRAL -> mergedGray;
        };
    }

    default AttributeHolder wrap() {
        return new AttributeHolder(self());
    }

    default MutableComponent toValueComponent(@Nullable PortAttributeModifier.Operation op, double value, TooltipFlag flag) {
        if (isNullOrAddition(op)) {
            return Component.translatable("portlib.value.flat", FORMAT.format(value));
        }

        return Component.translatable("portlib.value.percent", FORMAT.format(value * 100));
    }

    default MutableComponent toComponent(PortAttributeModifier modif, TooltipFlag flag) {
        double value = modif.amount();
        String key = value > 0 ? "portlib.modifier.plus" : "portlib.modifier.take";
        ChatFormatting color = getStyle(value > 0);

        Component attrDesc = Component.translatable(self().getDescriptionId());
        Component valueComp = toValueComponent(modif.operation(), value, flag);

        return Component.translatable(key, valueComp, attrDesc).withStyle(color);
    }





    static boolean isNullOrAddition(@Nullable PortAttributeModifier.Operation op) {
        return op == null || op == PortAttributeModifier.Operation.ADD_VALUE;
    }
}
