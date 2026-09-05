package org.mesdag.portlib.wrapper.common.extensions;

import PortLib.extensions.com.mojang.serialization.Codec.PortCodecExtension;
import com.mojang.serialization.Codec;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.network.FriendlyByteBuf;
import org.mesdag.portlib.network.codec.PortByteBufCodecs;
import org.mesdag.portlib.network.codec.PortStreamCodec;

@SuppressWarnings("all")
public interface IPortItemPredicateExtension {
    Codec<ItemPredicate> CODEC = PortCodecExtension.json(ItemPredicate::serializeToJson, ItemPredicate::fromJson);
    PortStreamCodec<FriendlyByteBuf, ItemPredicate> STREAM_CODEC = PortByteBufCodecs.json(ItemPredicate.ANY, ItemPredicate::serializeToJson, ItemPredicate::fromJson);


}
