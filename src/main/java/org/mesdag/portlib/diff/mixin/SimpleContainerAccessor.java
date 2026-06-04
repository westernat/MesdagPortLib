package org.mesdag.portlib.diff.mixin;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.gen.Accessor;

@org.spongepowered.asm.mixin.Mixin(net.minecraft.world.SimpleContainer.class)
public interface SimpleContainerAccessor {
    @Accessor
    NonNullList<ItemStack> getItems();
}
