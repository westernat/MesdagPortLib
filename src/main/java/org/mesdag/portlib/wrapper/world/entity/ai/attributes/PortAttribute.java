package org.mesdag.portlib.wrapper.world.entity.ai.attributes;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.entity.ai.attributes.Attribute;
import org.mesdag.portlib.diff.IPortAttribute;

public class PortAttribute {
    private static final TextColor MERGED_RED = TextColor.fromRgb(0xF93131);
    private static final TextColor MERGED_BLUE = TextColor.fromRgb(0x7A7AF9);
    private static final TextColor MERGED_GRAY = TextColor.fromRgb(0xCCCCCC);

    private final Attribute delegate;

    private PortAttribute(Attribute delegate) {
        this.delegate = delegate;
    }

    public Attribute unwrap() {
        return delegate;
    }

    public static PortAttribute wrap(Attribute delegate) {
        return new PortAttribute(delegate);
    }

    public ChatFormatting getStyle(boolean isPositive) {
        return IPortAttribute.of(delegate).portlib$getSentiment().getStyle(isPositive);
    }

    public TextColor getMergedStyle(boolean isPositive) {
        return switch (IPortAttribute.of(delegate).portlib$getSentiment()) {
            case POSITIVE -> isPositive ? MERGED_BLUE : MERGED_RED;
            case NEGATIVE -> isPositive ? MERGED_RED : MERGED_BLUE;
            case NEUTRAL -> MERGED_GRAY;
        };
    }

    public enum PortSentiment {
        POSITIVE,
        NEUTRAL,
        NEGATIVE;

        public ChatFormatting getStyle(boolean isPositive) {
            return switch (this) {
                case POSITIVE -> isPositive ? ChatFormatting.BLUE : ChatFormatting.RED;
                case NEUTRAL -> ChatFormatting.GRAY;
                case NEGATIVE -> isPositive ? ChatFormatting.RED : ChatFormatting.BLUE;
            };
        }
    }
}
