package org.mesdag.portlib.wrapper.world.entity.ai.attributes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.mesdag.portlib.diff.Diff;

import java.util.UUID;

public record PortAttributeModifier(ResourceLocation id, double amount, PortOperation operation) {
    private static final char[] HEX_DIGITS = "0123456789abcdef".toCharArray();

    @Diff
    public AttributeModifier unwrap() {
        return new AttributeModifier(namespaceToUUID(id.getNamespace()), id.getPath(), amount, operation.unwrap());
    }

    @Diff
    public static ResourceLocation toId(UUID uuid, String name) {
        String namespace = toHex16(uuid.getMostSignificantBits()) + toHex16(uuid.getLeastSignificantBits());
        return ResourceLocation.fromNamespaceAndPath(namespace, name);
    }

    private static String toHex16(long value) {
        char[] buf = new char[16];
        for (int i = 15; i >= 0; i--) {
            buf[i] = HEX_DIGITS[(int) (value & 0xF)];
            value >>>= 4;
        }
        return new String(buf);
    }

    private static UUID namespaceToUUID(String namespace) {
        try {
            long mostSig = Long.parseUnsignedLong(namespace.substring(0, 16), 16);
            long leastSig = Long.parseUnsignedLong(namespace.substring(16, 32), 16);
            return new UUID(mostSig, leastSig);
        } catch (Throwable e) {
            return UUID.nameUUIDFromBytes(namespace.getBytes());
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
