package org.mesdag.portlib.wrapper.world.item.component;

import com.google.common.collect.Lists;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.mesdag.portlib.diff.Diff;

import java.util.List;

public record PortChargedProjectiles(ItemStack crossbowStack) {
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
}
