package org.mesdag.portlib.wrapper.world.item.component;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ChargedProjectiles;
import org.mesdag.portlib.diff.Diff;

import java.util.List;

@SuppressWarnings("all")
public record PortChargedProjectiles(ItemStack crossbowStack) {
    public PortChargedProjectiles {
        if (crossbowStack.isEmpty()) {
            throw new IllegalArgumentException("CrossbowStack cannot be empty!");
        }
    }

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

    public void applyTo(ItemStack stack) {
        if (stack == crossbowStack) return;
        stack.set(DataComponents.CHARGED_PROJECTILES, crossbowStack.get(DataComponents.CHARGED_PROJECTILES));
    }
}
