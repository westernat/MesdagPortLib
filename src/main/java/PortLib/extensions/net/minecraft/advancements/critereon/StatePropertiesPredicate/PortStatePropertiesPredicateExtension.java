package PortLib.extensions.net.minecraft.advancements.critereon.StatePropertiesPredicate;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import org.mesdag.portlib.network.codec.PortStreamCodec;

public class PortStatePropertiesPredicateExtension {
    private static final Codec<StatePropertiesPredicate> CODEC = new Codec<>() {
        @Override
        public <T> DataResult<Pair<StatePropertiesPredicate, T>> decode(DynamicOps<T> ops, T input) {
            return DataResult.success(new Pair<>(StatePropertiesPredicate.fromJson(ops.convertTo(JsonOps.INSTANCE, input)), input), Lifecycle.stable());
        }

        @Override
        public <T> DataResult<T> encode(StatePropertiesPredicate input, DynamicOps<T> ops, T prefix) {
            return DataResult.success(JsonOps.INSTANCE.convertTo(ops, input.serializeToJson()), Lifecycle.stable());
        }
    };
    private static final PortStreamCodec<FriendlyByteBuf, StatePropertiesPredicate> STREAM_CODEC = new PortStreamCodec<>() {
        @Override
        public StatePropertiesPredicate decode(FriendlyByteBuf buffer) {
            CompoundTag tag = buffer.readAnySizeNbt();
            if (tag == null) {
                return StatePropertiesPredicate.ANY;
            }
            return StatePropertiesPredicate.fromJson(NbtOps.INSTANCE.convertTo(JsonOps.INSTANCE, tag));
        }

        @Override
        public void encode(FriendlyByteBuf buffer, StatePropertiesPredicate value) {
            buffer.writeNbt((CompoundTag) JsonOps.INSTANCE.convertTo(NbtOps.INSTANCE, value.serializeToJson()));
        }
    };

    public static Codec<StatePropertiesPredicate> codec() {
        return CODEC;
    }

    public static PortStreamCodec<FriendlyByteBuf, StatePropertiesPredicate> streamCodec() {
        return STREAM_CODEC;
    }
}
