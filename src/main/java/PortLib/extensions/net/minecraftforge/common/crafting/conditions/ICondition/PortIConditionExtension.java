package PortLib.extensions.net.minecraftforge.common.crafting.conditions.ICondition;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.mesdag.portlib.wrapper.PortUtil;

import java.util.List;

public class PortIConditionExtension {
    private static final Codec<ICondition> CODEC = new Codec<>() {
        @Override
        public <T> DataResult<Pair<ICondition, T>> decode(DynamicOps<T> ops, T input) {
            try {
                return DataResult.success(new Pair<>(CraftingHelper.getCondition(ops.convertTo(JsonOps.INSTANCE, input).getAsJsonObject()), input));
            } catch (Exception e) {
                return DataResult.error(e::getMessage);
            }
        }

        @Override
        public <T> DataResult<T> encode(ICondition input, DynamicOps<T> ops, T prefix) {
            try {
                return PortUtil.encode(input, CraftingHelper::serialize, ops, prefix);
            } catch (Exception e) {
                return DataResult.error(e::getMessage);
            }
        }
    };
    private static final Codec<List<ICondition>> LIST_CODEC = CODEC.listOf();

    public static Codec<ICondition> codec() {
        return CODEC;
    }

    public static Codec<List<ICondition>> listCodec() {
        return LIST_CODEC;
    }
}
