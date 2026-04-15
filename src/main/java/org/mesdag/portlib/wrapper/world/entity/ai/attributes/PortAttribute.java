package org.mesdag.portlib.wrapper.world.entity.ai.attributes;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.entity.ai.attributes.Attribute;
import org.mesdag.portlib.diff.Diff;

public class PortAttribute {
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
        return delegate.getStyle(isPositive);
    }

    public TextColor getMergedStyle(boolean isPositive) {
        return delegate.getMergedStyle(isPositive);
    }

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

        @Diff
        public static PortSentiment wrap(Attribute.Sentiment sentiment) {
            if (sentiment == Attribute.Sentiment.NEUTRAL) {
                return NEUTRAL;
            } else if (sentiment == Attribute.Sentiment.NEGATIVE) {
                return NEGATIVE;
            }
            return POSITIVE;
        }
    }
}
