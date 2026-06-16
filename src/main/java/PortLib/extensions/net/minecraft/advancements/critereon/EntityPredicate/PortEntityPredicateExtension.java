package PortLib.extensions.net.minecraft.advancements.critereon.EntityPredicate;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import net.minecraft.advancements.critereon.EntityPredicate;
import org.mesdag.portlib.wrapper.PortUtil;

public class PortEntityPredicateExtension {
    private static final Codec<EntityPredicate> CODEC = new Codec<>() {
        @Override
        public <T> DataResult<Pair<EntityPredicate, T>> decode(DynamicOps<T> ops, T input) {
            return DataResult.success(new Pair<>(EntityPredicate.fromJson(ops.convertTo(JsonOps.INSTANCE, input)), input));
        }

        @Override
        public <T> DataResult<T> encode(EntityPredicate input, DynamicOps<T> ops, T prefix) {
            return PortUtil.encode(input, EntityPredicate::serializeToJson, ops, prefix);
        }
    };

    public static Codec<EntityPredicate> codec() {
        return CODEC;
    }
}
