package org.mesdag.portlib.wrapper.world.entity.ai.attributes;

import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.ai.attributes.Attribute;
import org.mesdag.portlib.diff.Diff;

public class PortAttribute {
    public enum PortSentiment {
        POSITIVE,
        NEUTRAL,
        NEGATIVE;

        public ChatFormatting getStyle(boolean isPositive) {
            return unwrap().getStyle(isPositive);
        }

        @Diff
        public Attribute.Sentiment unwrap() {
            if (this == NEUTRAL) {
                return Attribute.Sentiment.NEUTRAL;
            } else if (this == NEGATIVE) {
                return Attribute.Sentiment.NEGATIVE;
            }
            return Attribute.Sentiment.POSITIVE;
        }
    }
}
