package PortLib.extensions.net.minecraft.world.entity.ai.attributes.AttributeModifier;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.mesdag.portlib.wrapper.world.entity.ai.attributes.PortAttributeModifier;

@Extension
public class PortAttributeModifierExtension {
    public static PortAttributeModifier wrap(@This AttributeModifier thiz) {
        return new PortAttributeModifier(thiz.id().wrap(), thiz.amount(), thiz.operation().wrap());
    }

    public static class Operation {
        public static PortAttributeModifier.PortOperation wrap(@This AttributeModifier.Operation thiz) {
            if (thiz == AttributeModifier.Operation.ADD_MULTIPLIED_BASE) {
                return PortAttributeModifier.PortOperation.ADD_MULTIPLIED_BASE;
            } else if (thiz == AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL) {
                return PortAttributeModifier.PortOperation.ADD_MULTIPLIED_TOTAL;
            }
            return PortAttributeModifier.PortOperation.ADD_VALUE;
        }
    }
}
