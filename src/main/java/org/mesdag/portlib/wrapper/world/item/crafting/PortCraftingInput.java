package org.mesdag.portlib.wrapper.world.item.crafting;

import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PortCraftingInput implements PortRecipeInput {
    public static final PortCraftingInput EMPTY = new PortCraftingInput(0, 0, List.of());
    private final int width;
    private final int height;
    private final List<ItemStack> items;
    private final StackedContents stackedContents = new StackedContents();
    private final int ingredientCount;

    public PortCraftingInput(int width, int height, List<ItemStack> item) {
        this.width = width;
        this.height = height;
        this.items = item;
        int count = 0;
        for (ItemStack itemstack : item) {
            if (!itemstack.isEmpty()) {
                ++count;
                stackedContents.accountStack(itemstack, 1);
            }
        }
        this.ingredientCount = count;
    }

    public static PortCraftingInput of(int width, int height, List<ItemStack> items) {
        return ofPositioned(width, height, items).input();
    }

    public static PortCraftingInput.Positioned ofPositioned(int width, int height, List<ItemStack> items) {
        if (width == 0 || height == 0) {
            return Positioned.EMPTY;
        }
        int i = width - 1;
        int j = 0;
        int k = height - 1;
        int l = 0;
        for (int i1 = 0; i1 < height; i1++) {
            boolean flag = true;
            for (int j1 = 0; j1 < width; j1++) {
                ItemStack itemstack = items.get(j1 + i1 * width);
                if (!itemstack.isEmpty()) {
                    i = Math.min(i, j1);
                    j = Math.max(j, j1);
                    flag = false;
                }
            }
            if (!flag) {
                k = Math.min(k, i1);
                l = Math.max(l, i1);
            }
        }
        int i2 = j - i + 1;
        int j2 = l - k + 1;
        if (i2 <= 0 || j2 <= 0) {
            return Positioned.EMPTY;
        } else if (i2 == width && j2 == height) {
            return new Positioned(new PortCraftingInput(width, height, items), i, k);
        }
        List<ItemStack> list = new ArrayList<>(i2 * j2);
        for (int k2 = 0; k2 < j2; k2++) {
            for (int k1 = 0; k1 < i2; k1++) {
                int l1 = k1 + i + (k2 + k) * width;
                list.add(items.get(l1));
            }
        }
        return new Positioned(new PortCraftingInput(i2, j2, list), i, k);
    }

    @Override
    public ItemStack getItem(int index) {
        return items.get(index);
    }

    public ItemStack getItem(int row, int column) {
        return items.get(row + column * width);
    }

    @Override
    public int size() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        return ingredientCount == 0;
    }

    public StackedContents stackedContents() {
        return stackedContents;
    }

    public List<ItemStack> items() {
        return items;
    }

    public int ingredientCount() {
        return ingredientCount;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    @Override
    public boolean equals(Object other) {
        return other == this || (other instanceof PortCraftingInput craftinginput && width == craftinginput.width
                && height == craftinginput.height
                && ingredientCount == craftinginput.ingredientCount
                && Objects.equals(items, craftinginput.items));
    }

    @Override
    public int hashCode() {
        int i = Objects.hashCode(items);
        i = 31 * i + width;
        return 31 * i + height;
    }

    public record Positioned(PortCraftingInput input, int left, int top) {
        public static final Positioned EMPTY = new Positioned(PortCraftingInput.EMPTY, 0, 0);
    }
}
