package org.mesdag.portlib.wrapper.common.extensions;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

@SuppressWarnings("all")
public interface IPortIngredientExtension {
    private Ingredient self() {
        return (Ingredient) this;
    }

    default boolean hasNoItems() {
        ItemStack[] items = self().getItems();
        if (items.length == 0) return true;
        if (items.length == 1) {
            ItemStack item = items[0];
            return item.getItem() == Items.BARRIER && item.getHoverName() instanceof MutableComponent hoverName && hoverName.getString().startsWith("Empty Tag: ");
        }
        return false;
    }

    static IPortIngredientExtension of(Ingredient ingredient) {
        return (IPortIngredientExtension) ingredient;
    }
}
