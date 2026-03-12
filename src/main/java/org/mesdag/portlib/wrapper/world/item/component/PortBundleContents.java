package org.mesdag.portlib.wrapper.world.item.component;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.math.Fraction;

import java.util.stream.Stream;

@SuppressWarnings("all")
public record PortBundleContents(ItemStack bundleStack) {
    public PortBundleContents {
        if (bundleStack.isEmpty()) {
            throw new IllegalArgumentException("BundleStack cannot be empty!");
        }
    }

    public ItemStack getItemUnsafe(int index) {
        return items().toList().get(index);
    }

    public Stream<ItemStack> itemCopyStream() {
        return items();
    }

    public Stream<ItemStack> items() {
        return BundleItem.getContents(bundleStack);
    }

    public Iterable<ItemStack> itemsCopy() {
        return items().toList();
    }

    public int size() {
        return items().mapToInt(i -> 1).sum();
    }

    public Fraction weight() {
        return Fraction.getFraction(1, BundleItem.getContentWeight(bundleStack) / BundleItem.MAX_WEIGHT);
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public void applyTo(ItemStack stack) {
        if (stack == bundleStack) return;
        CompoundTag tag = bundleStack.getTag();
        if (tag != null && tag.contains("Items", Tag.TAG_LIST)) {
            stack.getOrCreateTag().put("Items", tag.getList("Items", Tag.TAG_COMPOUND));
        }
    }
}
