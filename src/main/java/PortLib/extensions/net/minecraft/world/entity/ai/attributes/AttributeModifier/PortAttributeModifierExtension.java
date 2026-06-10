package PortLib.extensions.net.minecraft.world.entity.ai.attributes.AttributeModifier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.mesdag.portlib.network.codec.PortByteBufCodecs;
import org.mesdag.portlib.network.codec.PortStreamCodec;
import org.mesdag.portlib.wrapper.world.entity.ai.attributes.PortAttributeModifier;

public class PortAttributeModifierExtension {
    private static final Codec<AttributeModifier> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            UUIDUtil.STRING_CODEC.fieldOf("id").forGetter(AttributeModifier::getId),
            Codec.STRING.fieldOf("name").forGetter(AttributeModifier::getName),
            Codec.DOUBLE.fieldOf("amount").forGetter(AttributeModifier::getAmount),
            Operation.codec().fieldOf("operation").forGetter(AttributeModifier::getOperation)
    ).apply(instance, AttributeModifier::new));
    private static final PortStreamCodec<FriendlyByteBuf, AttributeModifier> STREAM_CODEC = PortStreamCodec.composite(
            PortByteBufCodecs.UUID, AttributeModifier::getId,
            PortByteBufCodecs.STRING_UTF8, AttributeModifier::getName,
            PortByteBufCodecs.DOUBLE, AttributeModifier::getAmount,
            Operation.streamCodec(), AttributeModifier::getOperation,
            AttributeModifier::new
    );

    public static Codec<AttributeModifier> codec() {
        return CODEC;
    }

    public static PortStreamCodec<FriendlyByteBuf, AttributeModifier> streamCodec() {
        return STREAM_CODEC;
    }

    public static PortAttributeModifier wrap(AttributeModifier thiz) {
        return new PortAttributeModifier(PortAttributeModifier.uuid2rl(thiz.getId()), thiz.getAmount(), Operation.wrap(thiz.getOperation()));
    }

    public static class Operation {
        private static final Codec<AttributeModifier.Operation> CODEC = PortAttributeModifier.PortOperation.CODEC.xmap(PortAttributeModifier.PortOperation::unwrap, Operation::wrap);
        private static final PortStreamCodec<ByteBuf, AttributeModifier.Operation> STREAM_CODEC = PortAttributeModifier.PortOperation.STREAM_CODEC.map(PortAttributeModifier.PortOperation::unwrap, Operation::wrap);

        public static Codec<AttributeModifier.Operation> codec() {
            return CODEC;
        }

        public static PortStreamCodec<ByteBuf, AttributeModifier.Operation> streamCodec() {
            return STREAM_CODEC;
        }

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
