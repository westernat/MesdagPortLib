package org.mesdag.portlib.wrapper.world.entity.ai.attributes;

import PortLib.extensions.net.minecraft.resources.ResourceLocation.PortResourceLocationExtension;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.network.codec.PortByteBufCodecs;
import org.mesdag.portlib.network.codec.PortStreamCodec;

import java.util.Locale;
import java.util.UUID;
import java.util.function.IntFunction;

public record PortAttributeModifier(ResourceLocation id, double amount, PortOperation operation) {
    public static final Codec<PortAttributeModifier> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("id").forGetter(PortAttributeModifier::id),
            Codec.DOUBLE.fieldOf("amount").forGetter(PortAttributeModifier::amount),
            PortOperation.CODEC.fieldOf("operation").forGetter(PortAttributeModifier::operation)
    ).apply(instance, PortAttributeModifier::new));
    public static final PortStreamCodec<ByteBuf, PortAttributeModifier> STREAM_CODEC = PortStreamCodec.composite(
            PortResourceLocationExtension.streamCodec(), PortAttributeModifier::id,
            PortByteBufCodecs.DOUBLE, PortAttributeModifier::amount,
            PortOperation.STREAM_CODEC, PortAttributeModifier::operation,
            PortAttributeModifier::new
    );

    private static final char[] HEX_DIGITS = "0123456789abcdef".toCharArray();

    @Diff
    public AttributeModifier unwrap() {
        return new AttributeModifier(RL2UUID(id), id.getPath(), amount, operation.unwrap());
    }

    @Diff
    public static ResourceLocation UUID2RL(UUID uuid) {
        return ResourceLocation.fromNamespaceAndPath(toHex16(uuid.getMostSignificantBits()), toHex16(uuid.getLeastSignificantBits()));
    }

    private static String toHex16(long value) {
        char[] buf = new char[16];
        for (int i = 15; i >= 0; i--) {
            buf[i] = HEX_DIGITS[(int) (value & 0xF)];
            value >>>= 4;
        }
        return new String(buf);
    }

    @Diff
    public static UUID RL2UUID(ResourceLocation id) {
        try {
            long mostSig = Long.parseUnsignedLong(id.getNamespace(), 16);
            long leastSig = Long.parseUnsignedLong(id.getPath(), 16);
            return new UUID(mostSig, leastSig);
        } catch (Throwable e) {
            return UUID.nameUUIDFromBytes((id.getNamespace() + id.getPath()).getBytes());
        }
    }

    public enum PortOperation implements StringRepresentable {
        ADD_VALUE,
        ADD_MULTIPLIED_BASE,
        ADD_MULTIPLIED_TOTAL;

        public static final IntFunction<PortOperation> BY_ID = ByIdMap.continuous(PortOperation::ordinal, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
        public static final Codec<PortOperation> CODEC = StringRepresentable.fromEnum(PortOperation::values);
        public static final PortStreamCodec<ByteBuf, PortOperation> STREAM_CODEC = PortByteBufCodecs.idMapper(BY_ID, PortOperation::ordinal);

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

        @Override
        public String getSerializedName() {
            return name().toLowerCase(Locale.ROOT);
        }
    }
}
