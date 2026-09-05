package org.mesdag.portlib.wrapper.common.extensions;

import PortLib.extensions.com.mojang.serialization.Codec.PortCodecExtension;
import com.mojang.serialization.Codec;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.network.FriendlyByteBuf;
import org.mesdag.portlib.network.codec.PortByteBufCodecs;
import org.mesdag.portlib.network.codec.PortStreamCodec;

@SuppressWarnings("all")
public interface IPortStatePropertiesPredicateExtension {
    Codec<StatePropertiesPredicate> CODEC = PortCodecExtension.json(StatePropertiesPredicate::serializeToJson, StatePropertiesPredicate::fromJson);
    PortStreamCodec<FriendlyByteBuf, StatePropertiesPredicate> STREAM_CODEC = PortByteBufCodecs.json(StatePropertiesPredicate.ANY, StatePropertiesPredicate::serializeToJson, StatePropertiesPredicate::fromJson);


}
