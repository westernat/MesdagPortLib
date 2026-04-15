package org.mesdag.portlib.wrapper.world.item.crafting;

import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.CompoundIngredient;
import org.mesdag.portlib.network.PortRegistryFriendlyByteBuf;
import org.mesdag.portlib.network.codec.PortStreamCodec;
import org.mesdag.portlib.wrapper.serialization.PortCodec;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class PortIngredient {
    public static final Codec<Ingredient> CODEC = makeIngredientCodec(true);
    public static final Codec<Ingredient> CODEC_NONEMPTY = makeIngredientCodec(false);
    public static final MapCodec<Ingredient> MAP_CODEC_NONEMPTY = makeIngredientMapCodec();
    public static final Codec<List<Ingredient>> LIST_CODEC = MAP_CODEC_NONEMPTY.codec().listOf();
    public static final Codec<List<Ingredient>> LIST_CODEC_NONEMPTY = PortCodec.validate(LIST_CODEC, list -> list.isEmpty() ? DataResult.error(() -> "Item array cannot be empty, at least one item must be defined") : DataResult.success(list));

    public static final PortStreamCodec<PortRegistryFriendlyByteBuf, Ingredient> CONTENTS_STREAM_CODEC = new PortStreamCodec<>() {
        @Override
        public void encode(PortRegistryFriendlyByteBuf buf, Ingredient ingredient) {
            ingredient.toNetwork(buf);
        }

        @Override
        public Ingredient decode(PortRegistryFriendlyByteBuf buf) {
            return Ingredient.fromNetwork(buf);
        }
    };

    private static Codec<Ingredient> makeIngredientCodec(boolean allowEmpty) {
        var listCodec = PortCodec.lazyInitialized(() -> allowEmpty ? LIST_CODEC : LIST_CODEC_NONEMPTY);
        return Codec.either(listCodec, makeIngredientMapCodec().codec()).xmap(
                either -> either.map(list -> CompoundIngredient.of(list.toArray(Ingredient[]::new)), Function.identity()),
                ingredient -> {
                    if (!ingredient.isVanilla()) {
                        if (ingredient instanceof CompoundIngredient compound) {
                            return Either.left((List<Ingredient>) compound.getChildren());
                        }
                    } else if (ingredient.values.length != 1) {
                        return Either.left(Stream.of(ingredient.values).map(v -> Ingredient.fromValues(Stream.of(v))).toList());
                    }
                    return Either.right(ingredient);
                });
    }

    private static MapCodec<Ingredient> makeIngredientMapCodec() {
        return new MapCodec<>() {
            @Override
            public <T> Stream<T> keys(DynamicOps<T> ops) {
                return Stream.of(
                        ops.createString("type"),
                        ops.createString("item"),
                        ops.createString("tag")
                );
            }

            @Override
            public <T> DataResult<Ingredient> decode(DynamicOps<T> ops, MapLike<T> input) {
                return ops.mergeToMap(ops.emptyMap(), input)
                        .map(t -> Ingredient.fromJson(ops.convertTo(JsonOps.INSTANCE, t), false));
            }

            @Override
            public <T> RecordBuilder<T> encode(Ingredient input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
                if (input.values.length != 1) {
                    throw new IllegalStateException("Only single ingredient value are supported");
                }
                for (Map.Entry<String, JsonElement> entry : input.values[0].serialize().asMap().entrySet()) {
                    prefix.add(entry.getKey(), JsonOps.INSTANCE.convertTo(ops, entry.getValue()));
                }
                return prefix;
            }
        };
    }
}
