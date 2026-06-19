package PortLib.extensions.net.minecraft.advancements.critereon.NbtPredicate;

import PortLib.extensions.com.mojang.serialization.Codec.PortCodecExtension;
import com.mojang.serialization.Codec;
import net.minecraft.advancements.critereon.NbtPredicate;
import net.minecraft.network.FriendlyByteBuf;
import org.mesdag.portlib.network.codec.PortByteBufCodecs;
import org.mesdag.portlib.network.codec.PortStreamCodec;

public class PortNbtPredicateExtension {
    private static final Codec<NbtPredicate> CODEC = PortCodecExtension.json(NbtPredicate::serializeToJson, NbtPredicate::fromJson);
    private static final PortStreamCodec<FriendlyByteBuf, NbtPredicate> STREAM_CODEC = PortByteBufCodecs.json(NbtPredicate.ANY, NbtPredicate::serializeToJson, NbtPredicate::fromJson);

    public static Codec<NbtPredicate> codec() {
        return CODEC;
    }

    public static PortStreamCodec<FriendlyByteBuf, NbtPredicate> streamCodec() {
        return STREAM_CODEC;
    }
}
