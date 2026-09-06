package org.mesdag.portlib.wrapper.world.item.crafting;

import PortLib.extensions.com.mojang.serialization.Codec.PortCodecExtension;
import PortLib.extensions.com.mojang.serialization.DataResult.PortDataResultExtension;
import PortLib.extensions.java.util.List.PortListExtension;
import com.google.common.annotations.VisibleForTesting;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.chars.CharArraySet;
import it.unimi.dsi.fastutil.chars.CharSet;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.mesdag.portlib.diff.mixin.ShapedRecipeAccessor;
import org.mesdag.portlib.network.PortRegistryFriendlyByteBuf;
import org.mesdag.portlib.network.codec.PortStreamCodec;
import org.mesdag.portlib.wrapper.PortUtil;
import org.mesdag.portlib.wrapper.common.extensions.IPortIngredientExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public final class PortShapedRecipePattern {
    public static int getMaxWidth() {
        return ShapedRecipeAccessor.getMAX_WIDTH();
    }

    public static int getMaxHeight() {
        return ShapedRecipeAccessor.getMAX_HEIGHT();
    }

    public static void setCraftingSize(int width, int height) {
        ShapedRecipe.setCraftingSize(width, height);
    }

    public static final MapCodec<PortShapedRecipePattern> MAP_CODEC = PortShapedRecipePattern.Data.MAP_CODEC.flatXmap(
            PortShapedRecipePattern::unpack,
            pattern -> pattern.data.map(DataResult::success).orElseGet(() -> DataResult.error(() -> "Cannot encode unpacked recipe"))
    );
    public static final PortStreamCodec<PortRegistryFriendlyByteBuf, PortShapedRecipePattern> STREAM_CODEC = PortStreamCodec.ofMember(
            PortShapedRecipePattern::toNetwork, PortShapedRecipePattern::fromNetwork
    );
    private final int width;
    private final int height;
    private final NonNullList<Ingredient> ingredients;
    public final Optional<PortShapedRecipePattern.Data> data;
    private final int ingredientCount;
    private final boolean symmetrical;

    private boolean nonSymmetricalMatching;

    public PortShapedRecipePattern(int width, int height, NonNullList<Ingredient> ingredients, Optional<PortShapedRecipePattern.Data> data) {
        this.width = width;
        this.height = height;
        this.ingredients = ingredients;
        this.data = data;
        int i = 0;

        for (Ingredient ingredient : ingredients) {
            if (!ingredient.isEmpty()) {
                i++;
            }
        }

        this.ingredientCount = i;
        this.symmetrical = PortUtil.isSymmetrical(width, height, ingredients);
    }

    public void setNonSymmetricalMatching() {
        this.nonSymmetricalMatching = true;
    }

    public static PortShapedRecipePattern of(Map<Character, Ingredient> key, String... pattern) {
        return of(key, List.of(pattern));
    }

    public static PortShapedRecipePattern of(Map<Character, Ingredient> key, List<String> pattern) {
        PortShapedRecipePattern.Data shapedrecipepattern$data = new PortShapedRecipePattern.Data(key, pattern);
        return PortDataResultExtension.getOrThrow(unpack(shapedrecipepattern$data));
    }

    private static DataResult<PortShapedRecipePattern> unpack(PortShapedRecipePattern.Data data) {
        String[] astring = shrink(data.pattern);
        int i = astring[0].length();
        int j = astring.length;
        NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i * j, Ingredient.EMPTY);
        CharSet charset = new CharArraySet(data.key.keySet());

        for (int k = 0; k < astring.length; k++) {
            String s = astring[k];

            for (int l = 0; l < s.length(); l++) {
                char c0 = s.charAt(l);
                Ingredient ingredient = c0 == ' ' ? Ingredient.EMPTY : data.key.get(c0);
                if (ingredient == null) {
                    return DataResult.error(() -> "Pattern references symbol '" + c0 + "' but it's not defined in the key");
                }

                charset.remove(c0);
                nonnulllist.set(l + i * k, ingredient);
            }
        }

        return !charset.isEmpty()
                ? DataResult.error(() -> "Key defines symbols that aren't used in pattern: " + charset)
                : DataResult.success(new PortShapedRecipePattern(i, j, nonnulllist, Optional.of(data)));
    }

    @VisibleForTesting
    static String[] shrink(List<String> pattern) {
        int i = Integer.MAX_VALUE;
        int j = 0;
        int k = 0;
        int l = 0;

        for (int i1 = 0; i1 < pattern.size(); i1++) {
            String s = pattern.get(i1);
            i = Math.min(i, firstNonSpace(s));
            int j1 = lastNonSpace(s);
            j = Math.max(j, j1);
            if (j1 < 0) {
                if (k == i1) {
                    k++;
                }

                l++;
            } else {
                l = 0;
            }
        }

        if (pattern.size() == l) {
            return new String[0];
        } else {
            String[] astring = new String[pattern.size() - l - k];

            for (int k1 = 0; k1 < astring.length; k1++) {
                astring[k1] = pattern.get(k1 + k).substring(i, j + 1);
            }

            return astring;
        }
    }

    private static int firstNonSpace(String row) {
        int i = 0;

        while (i < row.length() && row.charAt(i) == ' ') {
            i++;
        }

        return i;
    }

    private static int lastNonSpace(String row) {
        int i = row.length() - 1;

        while (i >= 0 && row.charAt(i) == ' ') {
            i--;
        }

        return i;
    }

    public boolean matches(PortCraftingInput input) {
        if (input.ingredientCount() == ingredientCount) {
            if (input.width() == width && input.height() == height) {
                if (!symmetrical && matches(input, true)) {
                    return true;
                }

                return matches(input, false);
            }

        }
        return false;
    }

    private boolean matches(PortCraftingInput input, boolean symmetrical) {
        if (nonSymmetricalMatching) {
            symmetrical = false;
        }
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Ingredient ingredient;
                if (symmetrical) {
                    ingredient = ingredients.get(width - j - 1 + i * width);
                } else {
                    ingredient = ingredients.get(j + i * width);
                }

                ItemStack itemstack = input.getItem(j, i);
                if (!ingredient.test(itemstack)) {
                    return false;
                }
            }
        }

        return true;
    }

    private void toNetwork(PortRegistryFriendlyByteBuf buffer) {
        buffer.writeVarInt(width);
        buffer.writeVarInt(height);

        for (Ingredient ingredient : ingredients) {
            IPortIngredientExtension.CONTENTS_STREAM_CODEC.encode(buffer, ingredient);
        }
    }

    private static PortShapedRecipePattern fromNetwork(PortRegistryFriendlyByteBuf buffer) {
        int width = buffer.readVarInt();
        int height = buffer.readVarInt();
        NonNullList<Ingredient> nonnulllist = NonNullList.withSize(width * height, Ingredient.EMPTY);
        nonnulllist.replaceAll(ingredient -> IPortIngredientExtension.CONTENTS_STREAM_CODEC.decode(buffer));
        return new PortShapedRecipePattern(width, height, nonnulllist, Optional.empty());
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public NonNullList<Ingredient> ingredients() {
        return ingredients;
    }

    public record Data(Map<Character, Ingredient> key, List<String> pattern) {
        private static final Codec<List<String>> PATTERN_CODEC = Codec.STRING.listOf().comapFlatMap(list -> {
            if (list.size() > getMaxHeight()) {
                return DataResult.error(() -> "Invalid pattern: too many rows, %s is maximum".formatted(getMaxHeight()));
            } else if (list.isEmpty()) {
                return DataResult.error(() -> "Invalid pattern: empty pattern not allowed");
            }
            int length = PortListExtension.getFirst(list).length();
            for (String s : list) {
                if (s.length() > getMaxWidth()) {
                    return DataResult.error(() -> "Invalid pattern: too many columns, %s is maximum".formatted(getMaxWidth()));
                }
                if (length != s.length()) {
                    return DataResult.error(() -> "Invalid pattern: each row must be the same width");
                }
            }
            return DataResult.success(list);
        }, Function.identity());
        private static final Codec<Character> SYMBOL_CODEC = Codec.STRING.comapFlatMap(symbol -> {
            if (symbol.length() != 1) {
                return DataResult.error(() -> "Invalid key entry: '" + symbol + "' is an invalid symbol (must be 1 character only).");
            }
            return " ".equals(symbol) ? DataResult.error(() -> "Invalid key entry: ' ' is a reserved symbol.") : DataResult.success(symbol.charAt(0));
        }, String::valueOf);
        public static final MapCodec<PortShapedRecipePattern.Data> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                PortCodecExtension.strictUnboundedMap(SYMBOL_CODEC, IPortIngredientExtension.CODEC_NONEMPTY).fieldOf("key").forGetter(data -> data.key),
                PATTERN_CODEC.fieldOf("pattern").forGetter(data -> data.pattern)
        ).apply(instance, PortShapedRecipePattern.Data::new));
    }
}
