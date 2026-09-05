package org.mesdag.portlib.wrapper.common.extensions;

import PortLib.extensions.com.mojang.serialization.Codec.PortCodecExtension;
import com.mojang.serialization.Codec;
import net.minecraft.advancements.critereon.NbtPredicate;
import net.minecraft.network.FriendlyByteBuf;
import org.mesdag.portlib.network.codec.PortByteBufCodecs;
import org.mesdag.portlib.network.codec.PortStreamCodec;

@SuppressWarnings("all")
public interface IPortNbtPredicateExtension {
    Codec<NbtPredicate> CODEC = PortCodecExtension.json(NbtPredicate::serializeToJson, NbtPredicate::fromJson);
    PortStreamCodec<FriendlyByteBuf, NbtPredicate> STREAM_CODEC = PortByteBufCodecs.json(NbtPredicate.ANY, NbtPredicate::serializeToJson, NbtPredicate::fromJson);


}
