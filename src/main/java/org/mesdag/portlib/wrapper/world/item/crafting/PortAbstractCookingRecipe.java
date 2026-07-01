package org.mesdag.portlib.wrapper.world.item.crafting;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;

public abstract class PortAbstractCookingRecipe extends AbstractCookingRecipe {
    public PortAbstractCookingRecipe(RecipeType<?> type, ResourceLocation id, String group, CookingBookCategory category, Ingredient ingredient, ItemStack result, float experience, int cookingTime) {
        super(type, id, group, category, ingredient, result, experience, cookingTime);
    }

    @FunctionalInterface
    public interface Factory<R extends AbstractCookingRecipe> {
        R create(ResourceLocation id, String group, CookingBookCategory category, Ingredient ingredient, ItemStack result, float experience, int cookingTime);
    }
}
