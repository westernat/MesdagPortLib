package PortLib.extensions.net.minecraft.world.entity.ai.attributes.Attribute;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.minecraft.world.entity.ai.attributes.Attribute;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.world.entity.ai.attributes.PortAttribute;

@Extension
public class PortAttributeExtension {
    public static class Sentiment {
        @Diff
        public static PortAttribute.PortSentiment wrap(@This Attribute.Sentiment thiz) {
            if (thiz == Attribute.Sentiment.NEUTRAL) {
                return PortAttribute.PortSentiment.NEUTRAL;
            } else if (thiz == Attribute.Sentiment.NEGATIVE) {
                return PortAttribute.PortSentiment.NEGATIVE;
            }
            return PortAttribute.PortSentiment.POSITIVE;
        }
    }
}
