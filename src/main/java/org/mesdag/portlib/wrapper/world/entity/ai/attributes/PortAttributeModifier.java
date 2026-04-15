package org.mesdag.portlib.wrapper.world.entity.ai.attributes;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.resources.PortIdentifier;

import java.util.UUID;

public record PortAttributeModifier(PortIdentifier id, double amount, PortOperation operation) {
    @Diff
    public AttributeModifier unwrap() {
        return new AttributeModifier(namespaceToUUID(id.getNamespace()), id.getPath(), amount, operation.unwrap());
    }

    @Diff
    public static PortAttributeModifier wrap(AttributeModifier modifier) {
        return new PortAttributeModifier(toId(modifier.getId(), modifier.getName()), modifier.getAmount(), PortOperation.wrap(modifier.getOperation()));
    }

    private static PortIdentifier toId(UUID uuid, String name) {
        return PortIdentifier.fromNamespaceAndPath(uuidToNamespace(uuid), name);
    }

    private static String uuidToNamespace(UUID uuid) {
        return String.format("%016x", uuid.getMostSignificantBits()) +
                String.format("%016x", uuid.getLeastSignificantBits());
    }

    private static UUID namespaceToUUID(String namespace) {
        if (namespace.length() != 32) {
            return UUID.fromString(namespace);
        }
        try {
            long mostSig = Long.parseUnsignedLong(namespace.substring(0, 16), 16);
            long leastSig = Long.parseUnsignedLong(namespace.substring(16, 32), 16);
            return new UUID(mostSig, leastSig);
        } catch (Exception e) {
            return UUID.fromString(namespace);
        }
    }

    public enum PortOperation {
        ADD_VALUE,
        ADD_MULTIPLIED_BASE,
        ADD_MULTIPLIED_TOTAL;

        @Diff
        public AttributeModifier.Operation unwrap() {
            if (this == ADD_MULTIPLIED_BASE) {
                return AttributeModifier.Operation.MULTIPLY_BASE;
            } else if (this == ADD_MULTIPLIED_TOTAL) {
                return AttributeModifier.Operation.MULTIPLY_TOTAL;
            }
            return AttributeModifier.Operation.ADDITION;
        }

        @Diff
        public static PortOperation wrap(AttributeModifier.Operation operation) {
            if (operation == AttributeModifier.Operation.MULTIPLY_BASE) {
                return ADD_MULTIPLIED_BASE;
            } else if (operation == AttributeModifier.Operation.MULTIPLY_TOTAL) {
                return ADD_MULTIPLIED_TOTAL;
            }
            return ADD_VALUE;
        }
    }
}
