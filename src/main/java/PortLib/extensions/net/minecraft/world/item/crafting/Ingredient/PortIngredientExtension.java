package PortLib.extensions.net.minecraft.world.item.crafting.Ingredient;

import PortLib.extensions.com.mojang.serialization.Codec.PortCodecExtension;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.*;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.AbstractIngredient;
import net.minecraftforge.common.crafting.CompoundIngredient;
import org.mesdag.portlib.network.PortRegistryFriendlyByteBuf;
import org.mesdag.portlib.network.codec.PortStreamCodec;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class PortIngredientExtension {
    private static final Codec<Ingredient> CODEC = makeIngredientCodec(true);
    private static final Codec<Ingredient> CODEC_NONEMPTY = makeIngredientCodec(false);
    private static final MapCodec<Ingredient> MAP_CODEC_NONEMPTY = makeIngredientMapCodec();
    private static final Codec<List<Ingredient>> LIST_CODEC = MAP_CODEC_NONEMPTY.codec().listOf();
    private static final Codec<List<Ingredient>> LIST_CODEC_NONEMPTY = PortCodecExtension.validate(LIST_CODEC, list -> list.isEmpty() ? DataResult.error(() -> "Item array cannot be empty, at least one item must be defined") : DataResult.success(list));

    private static final PortStreamCodec<PortRegistryFriendlyByteBuf, Ingredient> CONTENTS_STREAM_CODEC = new PortStreamCodec<>() {
        @Override
        public void encode(PortRegistryFriendlyByteBuf buf, Ingredient ingredient) {
            ingredient.toNetwork(buf);
        }

        @Override
        public Ingredient decode(PortRegistryFriendlyByteBuf buf) {
            return Ingredient.fromNetwork(buf);
        }
    };

    public static Codec<Ingredient> codec() {
        return CODEC;
    }

    public static Codec<Ingredient> codecNonempty() {
        return CODEC_NONEMPTY;
    }

    public static MapCodec<Ingredient> mapCodecNonempty() {
        return MAP_CODEC_NONEMPTY;
    }

    public static Codec<List<Ingredient>> listCodec() {
        return LIST_CODEC;
    }

    public static Codec<List<Ingredient>> listCodecNonempty() {
        return LIST_CODEC_NONEMPTY;
    }

    public static PortStreamCodec<PortRegistryFriendlyByteBuf, Ingredient> contentsStreamCodec() {
        return CONTENTS_STREAM_CODEC;
    }

    private static Codec<Ingredient> makeIngredientCodec(boolean allowEmpty) {
        var listCodec = PortCodecExtension.lazyInitialized(() -> allowEmpty ? LIST_CODEC : LIST_CODEC_NONEMPTY);
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
                JsonObject object;
                if (input instanceof AbstractIngredient ingredient) {
                    object = ingredient.toJson().getAsJsonObject();
                } else if (input.values.length != 1) {
                    throw new IllegalStateException("Only single ingredient value are supported");
                } else {
                    object = input.values[0].serialize();
                }
                for (Map.Entry<String, JsonElement> entry : object.asMap().entrySet()) {
                    prefix.add(entry.getKey(), JsonOps.INSTANCE.convertTo(ops, entry.getValue()));
                }
                return prefix;
            }
        };
    }

    public static boolean hasNoItems(Ingredient thiz) {
        ItemStack[] items = thiz.getItems();
        if (items.length == 0) return true;
        if (items.length == 1) {
            ItemStack item = items[0];
            return item.getItem() == Items.BARRIER && item.getHoverName() instanceof MutableComponent hoverName && hoverName.getString().startsWith("Empty Tag: ");
        }
        return false;
    }
}
