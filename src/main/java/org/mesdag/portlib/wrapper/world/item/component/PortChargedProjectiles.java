package org.mesdag.portlib.wrapper.world.item.component;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ChargedProjectiles;
import org.mesdag.portlib.diff.Diff;

import java.util.List;

public record PortChargedProjectiles(ItemStack crossbowStack) {
    @Diff
    public ChargedProjectiles unwrap() {
        return crossbowStack.getOrDefault(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY);
    }

    public boolean contains(Item item) {
        return unwrap().contains(item);
    }

    public List<ItemStack> getItems() {
        return unwrap().getItems();
    }

    public boolean isEmpty() {
        return unwrap().isEmpty();
    }
}
