package PortLib.extensions.net.minecraft.world.entity.ai.attributes.Attribute;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.entity.ai.attributes.Attribute;
import org.mesdag.portlib.diff.IPortAttribute;

@Extension
public class PortAttributeExtension {
    private static final TextColor MERGED_RED = TextColor.fromRgb(0xF93131);
    private static final TextColor MERGED_BLUE = TextColor.fromRgb(0x7A7AF9);
    private static final TextColor MERGED_GRAY = TextColor.fromRgb(0xCCCCCC);

    public static ChatFormatting getStyle(@This Attribute thiz, boolean isPositive) {
        return IPortAttribute.of(thiz).portlib$getSentiment().getStyle(isPositive);
    }

    public static TextColor getMergedStyle(@This Attribute thiz, boolean isPositive) {
        return switch (IPortAttribute.of(thiz).portlib$getSentiment()) {
            case POSITIVE -> isPositive ? MERGED_BLUE : MERGED_RED;
            case NEGATIVE -> isPositive ? MERGED_RED : MERGED_BLUE;
            case NEUTRAL -> MERGED_GRAY;
        };
    }
}
