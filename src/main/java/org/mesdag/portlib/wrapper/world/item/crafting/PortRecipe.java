package org.mesdag.portlib.wrapper.world.item.crafting;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;

public interface PortRecipe<I extends PortRecipeInput> extends Recipe<I> {
    void setId(ResourceLocation id);
}
