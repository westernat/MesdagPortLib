package org.mesdag.portlib.wrapper.common.extensions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.mesdag.portlib.network.codec.PortByteBufCodecs;
import org.mesdag.portlib.network.codec.PortStreamCodec;
import org.mesdag.portlib.wrapper.world.entity.ai.attributes.PortAttributeModifier;

@SuppressWarnings("all")
public interface IPortAttributeModifierExtension {
    MapCodec<AttributeModifier> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            UUIDUtil.STRING_CODEC.fieldOf("id").forGetter(AttributeModifier::getId),
            Codec.STRING.fieldOf("name").forGetter(AttributeModifier::getName),
            Codec.DOUBLE.fieldOf("amount").forGetter(AttributeModifier::getAmount),
            Operation.CODEC.fieldOf("operation").forGetter(AttributeModifier::getOperation)
    ).apply(instance, AttributeModifier::new));
    Codec<AttributeModifier> CODEC = MAP_CODEC.codec();
    PortStreamCodec<FriendlyByteBuf, AttributeModifier> STREAM_CODEC = PortStreamCodec.composite(
            PortByteBufCodecs.UUID, AttributeModifier::getId,
            PortByteBufCodecs.STRING_UTF8, AttributeModifier::getName,
            PortByteBufCodecs.DOUBLE, AttributeModifier::getAmount,
            Operation.STREAM_CODEC, AttributeModifier::getOperation,
            AttributeModifier::new
    );

    default PortAttributeModifier wrap() {
        return new PortAttributeModifier(PortAttributeModifier.uuid2rl(((AttributeModifier) (Object) this).getId()), ((AttributeModifier) (Object) this).getAmount(), Operation.wrap(((AttributeModifier) (Object) this).getOperation()));
    }

    static IPortAttributeModifierExtension of(AttributeModifier modifier) {
        return (IPortAttributeModifierExtension) (Object) modifier;
    }

    interface Operation {
        Codec<AttributeModifier.Operation> CODEC = PortAttributeModifier.Operation.CODEC.xmap(PortAttributeModifier.Operation::unwrap, IPortAttributeModifierExtension.Operation::wrap);
        PortStreamCodec<ByteBuf, AttributeModifier.Operation> STREAM_CODEC = PortAttributeModifier.Operation.STREAM_CODEC.map(PortAttributeModifier.Operation::unwrap, IPortAttributeModifierExtension.Operation::wrap);

        static PortAttributeModifier.Operation wrap(AttributeModifier.Operation thiz) {
            if (thiz == AttributeModifier.Operation.MULTIPLY_BASE) {
                return PortAttributeModifier.Operation.ADD_MULTIPLIED_BASE;
            } else if (thiz == AttributeModifier.Operation.MULTIPLY_TOTAL) {
                return PortAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL;
            }
            return PortAttributeModifier.Operation.ADD_VALUE;
        }
    }
}
