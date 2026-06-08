package PortLib.extensions.net.minecraft.world.entity.ai.attributes.AttributeModifier;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.mesdag.portlib.wrapper.world.entity.ai.attributes.PortAttributeModifier;

public class PortAttributeModifierExtension {
    public static PortAttributeModifier wrap(AttributeModifier thiz) {
        return new PortAttributeModifier(PortAttributeModifier.UUID2RL(thiz.getId()), thiz.getAmount(), Operation.wrap(thiz.getOperation()));
    }

    public static class Operation {
        public static PortAttributeModifier.PortOperation wrap(AttributeModifier.Operation thiz) {
            if (thiz == AttributeModifier.Operation.MULTIPLY_BASE) {
                return PortAttributeModifier.PortOperation.ADD_MULTIPLIED_BASE;
            } else if (thiz == AttributeModifier.Operation.MULTIPLY_TOTAL) {
                return PortAttributeModifier.PortOperation.ADD_MULTIPLIED_TOTAL;
            }
            return PortAttributeModifier.PortOperation.ADD_VALUE;
        }
    }
}
