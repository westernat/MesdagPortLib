package org.mesdag.portlib.wrapper.world.entity.ai.attributes;

import net.minecraft.ChatFormatting;

public class PortAttribute {
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
