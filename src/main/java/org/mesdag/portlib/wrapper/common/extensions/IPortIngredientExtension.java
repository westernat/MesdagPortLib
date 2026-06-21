package org.mesdag.portlib.wrapper.common.extensions;

import PortLib.extensions.net.minecraft.world.item.crafting.Ingredient.PortIngredientExtension;
import net.minecraft.world.item.crafting.Ingredient;

@SuppressWarnings("all")
public interface IPortIngredientExtension {

    private Ingredient self() {
        return (Ingredient) this;
    }

    default boolean hasNoItems() {
        return PortIngredientExtension.hasNoItems(self());
    }

    static IPortIngredientExtension of(Ingredient ingredient) {
        return (IPortIngredientExtension) ingredient;
    }
}
