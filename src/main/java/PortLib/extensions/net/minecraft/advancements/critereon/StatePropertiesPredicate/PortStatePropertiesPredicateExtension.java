package PortLib.extensions.net.minecraft.advancements.critereon.StatePropertiesPredicate;

import PortLib.extensions.com.mojang.serialization.Codec.PortCodecExtension;
import com.mojang.serialization.Codec;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.network.FriendlyByteBuf;
import org.mesdag.portlib.network.codec.PortByteBufCodecs;
import org.mesdag.portlib.network.codec.PortStreamCodec;

public class PortStatePropertiesPredicateExtension {
    private static final Codec<StatePropertiesPredicate> CODEC = PortCodecExtension.json(StatePropertiesPredicate::serializeToJson, StatePropertiesPredicate::fromJson);
    private static final PortStreamCodec<FriendlyByteBuf, StatePropertiesPredicate> STREAM_CODEC = PortByteBufCodecs.json(StatePropertiesPredicate.ANY, StatePropertiesPredicate::serializeToJson, StatePropertiesPredicate::fromJson);

    public static Codec<StatePropertiesPredicate> codec() {
        return CODEC;
    }

    public static PortStreamCodec<FriendlyByteBuf, StatePropertiesPredicate> streamCodec() {
        return STREAM_CODEC;
    }
}
