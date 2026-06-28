package org.mesdag.portlib.wrapper.common.extensions;

import net.minecraft.core.NonNullList;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

public interface ISimpleContainerExtension {
    default NonNullList<ItemStack> getItems() {
        return ((SimpleContainer) this).items;
    }
}
