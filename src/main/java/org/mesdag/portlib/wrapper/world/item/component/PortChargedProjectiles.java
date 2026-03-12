package org.mesdag.portlib.wrapper.world.item.component;

import com.google.common.collect.Lists;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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
    public List<ItemStack> unwrap() {
        return CrossbowItem.getChargedProjectiles(crossbowStack);
    }

    public boolean contains(Item item) {
        for (ItemStack itemstack : unwrap()) {
            if (itemstack.is(item)) {
                return true;
            }
        }
        return false;
    }

    public List<ItemStack> getItems() {
        return Lists.transform(unwrap(), ItemStack::copy);
    }

    public boolean isEmpty() {
        return unwrap().isEmpty();
    }

    public void applyTo(ItemStack stack) {
        if (stack == crossbowStack) return;
        CompoundTag tag = crossbowStack.getTag();
        if (tag != null && tag.contains("ChargedProjectiles", Tag.TAG_LIST)) {
            stack.getOrCreateTag().put(
                    "ChargedProjectiles",
                    tag.getList("ChargedProjectiles", Tag.TAG_COMPOUND).copy()
            );
        }
    }
}
