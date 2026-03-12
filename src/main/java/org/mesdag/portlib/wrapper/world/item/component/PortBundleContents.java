package org.mesdag.portlib.wrapper.world.item.component;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BundleContents;
import org.apache.commons.lang3.math.Fraction;
import org.mesdag.portlib.diff.Diff;

import java.util.stream.Stream;

public record PortBundleContents(ItemStack bundleStack) {
    public PortBundleContents {
        if (bundleStack.isEmpty()) {
            throw new IllegalArgumentException("BundleStack cannot be empty!");
        }
    }

    @Diff
    public BundleContents unwrap() {
        return bundleStack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
    }

    public ItemStack getItemUnsafe(int index) {
        return unwrap().getItemUnsafe(index);
    }

    public Stream<ItemStack> itemCopyStream() {
        return unwrap().itemCopyStream();
    }

    public Iterable<ItemStack> items() {
        return unwrap().items();
    }

    public Iterable<ItemStack> itemsCopy() {
        return unwrap().itemsCopy();
    }

    public int size() {
        return unwrap().size();
    }

    public Fraction weight() {
        return unwrap().weight();
    }

    public boolean isEmpty() {
        return unwrap().isEmpty();
    }
}
