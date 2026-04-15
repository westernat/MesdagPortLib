package org.mesdag.portlib.wrapper.common.crafting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.AbstractIngredient;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.mesdag.portlib.diff.Diff;

import java.util.stream.Stream;

public abstract class PortCustomIngredient extends AbstractIngredient {
    @Override
    public abstract boolean test(ItemStack stack);

    public abstract Stream<ItemStack> getItemStream();

    @Override
    public abstract boolean isSimple();

    @Contract(pure = true)
    public abstract PortIngredientType<? extends PortCustomIngredient> getIngredientType();

    @ApiStatus.NonExtendable
    public Ingredient toVanilla() {
        return this;
    }

    @Diff
    @ApiStatus.NonExtendable
    @Override
    public ItemStack[] getItems() {
        return getItemStream().toArray(ItemStack[]::new);
    }

    @Diff
    @ApiStatus.NonExtendable
    @Override
    public IIngredientSerializer<? extends PortCustomIngredient> getSerializer() {
        return getIngredientType().serializer();
    }

    @Diff
    @ApiStatus.NonExtendable
    @Override
    public JsonElement toJson() {
        MapCodec<PortCustomIngredient> codec = (MapCodec<PortCustomIngredient>) getIngredientType().codec();
        DynamicOps<JsonElement> ops = JsonOps.INSTANCE;
        return codec.encode(this, ops, codec.compressedBuilder(ops)).build(ops.empty()).result().orElseGet(JsonObject::new);
    }
}
