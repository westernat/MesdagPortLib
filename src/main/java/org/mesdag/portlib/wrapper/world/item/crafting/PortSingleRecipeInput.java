package org.mesdag.portlib.wrapper.world.item.crafting;

import net.minecraft.world.item.ItemStack;

public record PortSingleRecipeInput(ItemStack item) implements PortRecipeInput {
    @Override
    public ItemStack getItem(int index) {
        if (index != 0) {
            throw new IllegalArgumentException("No item for index " + index);
        }
        return this.item;
    }

    @Override
    public int size() {
        return 1;
    }
}
