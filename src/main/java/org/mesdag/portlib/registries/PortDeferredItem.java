package org.mesdag.portlib.registries;

import com.google.common.base.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.RegistryObject;

public class PortDeferredItem<T extends Item> extends PortRegistryEntry<Item, T> implements ItemLike {
    public PortDeferredItem(ResourceLocation identifier, Supplier<T> valueSupplier) {
        super(identifier, valueSupplier);
    }

    public static <T extends Item> PortDeferredItem<T> createItem(ResourceLocation id) {
        PortDeferredItem<T> block = new PortDeferredItem<>(id, null);
        block.object = RegistryObject.createOptional(id, Registries.ITEM, id.getNamespace());
        return block;
    }

    @Override
    public Item asItem() {
        return get();
    }

    public ItemStack toStack(int count) {
        ItemStack stack = get().getDefaultInstance();
        stack.setCount(count);
        return stack;
    }

    public ItemStack toStack() {
        return toStack(1);
    }
}
