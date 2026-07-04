package org.mesdag.portlib.registries;

import com.google.common.base.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

public class PortDeferredBlock<T extends Block> extends PortRegistryEntry<Block, T> implements ItemLike {
    public PortDeferredBlock(ResourceLocation identifier, @Nullable Supplier<T> valueSupplier) {
        super(identifier, valueSupplier);
    }

    public static <T extends Block> PortDeferredBlock<T> createBlock(ResourceLocation id) {
        PortDeferredBlock<T> block = new PortDeferredBlock<>(id, null);
        block.object = RegistryObject.createOptional(id, Registries.BLOCK, id.getNamespace());
        return block;
    }

    @Override
    public Item asItem() {
        return get().asItem();
    }

    public ItemStack toStack(int count) {
        ItemStack stack = asItem().getDefaultInstance();
        stack.setCount(count);
        return stack;
    }

    public ItemStack toStack() {
        return toStack(1);
    }
}
