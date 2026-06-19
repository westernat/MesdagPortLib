package PortLib.extensions.net.minecraft.advancements.critereon.ItemPredicate;

import PortLib.extensions.com.mojang.serialization.Codec.PortCodecExtension;
import com.mojang.serialization.Codec;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.network.FriendlyByteBuf;
import org.mesdag.portlib.network.codec.PortByteBufCodecs;
import org.mesdag.portlib.network.codec.PortStreamCodec;

public class PortItemPredicateExtension {
    private static final Codec<ItemPredicate> CODEC = PortCodecExtension.json(ItemPredicate::serializeToJson, ItemPredicate::fromJson);
    private static final PortStreamCodec<FriendlyByteBuf, ItemPredicate> STREAM_CODEC = PortByteBufCodecs.json(ItemPredicate.ANY, ItemPredicate::serializeToJson, ItemPredicate::fromJson);

    public static Codec<ItemPredicate> codec() {
        return CODEC;
    }

    public static PortStreamCodec<FriendlyByteBuf, ItemPredicate> streamCodec() {
        return STREAM_CODEC;
    }
}
