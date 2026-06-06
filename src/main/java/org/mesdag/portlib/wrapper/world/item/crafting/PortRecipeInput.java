package org.mesdag.portlib.wrapper.world.item.crafting;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;

public interface PortRecipeInput extends Container {
    ItemStack getItem(int index);

    int size();

    @Override
    default boolean isEmpty() {
        for (int i = 0; i < size(); i++) {
            if (!getItem(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    // Non Extendable Start

    @ApiStatus.NonExtendable
    @Override
    default int getContainerSize() {
        return size();
    }

    @ApiStatus.NonExtendable
    @Override
    default ItemStack removeItem(int slot, int amount) {
        return ItemStack.EMPTY;
    }

    @ApiStatus.NonExtendable
    @Override
    default ItemStack removeItemNoUpdate(int slot) {
        return ItemStack.EMPTY;
    }

    @ApiStatus.NonExtendable
    @Override
    default void setItem(int slot, ItemStack stack) {}

    @ApiStatus.NonExtendable
    @Override
    default void setChanged() {}

    @ApiStatus.NonExtendable
    @Override
    default boolean stillValid(Player player) {
        return true;
    }

    @ApiStatus.NonExtendable
    @Override
    default void clearContent() {}
}
