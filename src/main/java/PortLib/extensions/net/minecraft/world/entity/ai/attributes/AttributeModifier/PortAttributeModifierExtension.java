package PortLib.extensions.net.minecraft.world.entity.ai.attributes.AttributeModifier;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.mesdag.portlib.wrapper.world.entity.ai.attributes.PortAttributeModifier;

@Extension
public class PortAttributeModifierExtension {
    public static PortAttributeModifier wrap(@This AttributeModifier thiz) {
        return new PortAttributeModifier(PortAttributeModifier.toId(thiz.getId(), thiz.getName()), thiz.getAmount(), thiz.getOperation().wrap());
    }

    public static class Operation {
        public static PortAttributeModifier.PortOperation wrap(@This AttributeModifier.Operation thiz) {
            if (thiz == AttributeModifier.Operation.MULTIPLY_BASE) {
                return PortAttributeModifier.PortOperation.ADD_MULTIPLIED_BASE;
            } else if (thiz == AttributeModifier.Operation.MULTIPLY_TOTAL) {
                return PortAttributeModifier.PortOperation.ADD_MULTIPLIED_TOTAL;
            }
            return PortAttributeModifier.PortOperation.ADD_VALUE;
        }
    }
}
