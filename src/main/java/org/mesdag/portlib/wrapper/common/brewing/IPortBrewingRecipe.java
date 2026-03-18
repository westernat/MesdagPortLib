package org.mesdag.portlib.wrapper.common.brewing;

import net.minecraft.world.item.ItemStack;

public interface IPortBrewingRecipe {
    boolean isInput(ItemStack input);

    boolean isIngredient(ItemStack ingredient);

    ItemStack getOutput(ItemStack input, ItemStack ingredient);
}
