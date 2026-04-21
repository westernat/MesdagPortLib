package org.mesdag.portlib.wrapper.world.entity.ai.attributes;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.resources.PortIdentifier;

public record PortAttributeModifier(PortIdentifier id, double amount, PortOperation operation) {
    @Diff
    public AttributeModifier unwrap() {
        return new AttributeModifier(id, amount, operation.unwrap());
    }

    public enum PortOperation {
        ADD_VALUE,
        ADD_MULTIPLIED_BASE,
        ADD_MULTIPLIED_TOTAL;

        @Diff
        public AttributeModifier.Operation unwrap() {
            if (this == ADD_MULTIPLIED_BASE) {
                return AttributeModifier.Operation.ADD_MULTIPLIED_BASE;
            } else if (this == ADD_MULTIPLIED_TOTAL) {
                return AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL;
            }
            return AttributeModifier.Operation.ADD_VALUE;
        }
    }
}
