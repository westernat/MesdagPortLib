package org.mesdag.portlib.wrapper.common.crafting;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.crafting.ICustomIngredient;
import net.neoforged.neoforge.common.crafting.IngredientType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.mesdag.portlib.diff.Diff;

import java.util.stream.Stream;

public abstract class PortCustomIngredient implements ICustomIngredient {
    @Override
    public abstract boolean test(ItemStack stack);

    public abstract Stream<ItemStack> getItemStream();

    @Override
    public abstract boolean isSimple();

    @Contract(pure = true)
    public abstract PortIngredientType<?> getIngredientType();

    @Diff
    @ApiStatus.NonExtendable
    @Override
    public Stream<ItemStack> getItems() {
        return getItemStream();
    }

    @Diff
    @ApiStatus.NonExtendable
    @Override
    public IngredientType<? extends PortCustomIngredient> getType() {
        return getIngredientType().unwrap();
    }
}
