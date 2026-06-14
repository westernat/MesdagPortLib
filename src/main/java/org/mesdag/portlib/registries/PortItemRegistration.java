package org.mesdag.portlib.registries;

import com.google.common.base.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Function;

public class PortItemRegistration extends PortRegistration<Item> {
    PortItemRegistration(String namespace) {
        super(namespace, Registries.ITEM);
    }

    @Override
    public <T extends Item> PortDeferredItem<T> register(String name, Supplier<T> valueSupplier) {
        ResourceLocation id = asId(name);
        PortDeferredItem<T> entry = new PortDeferredItem<>(id, valueSupplier);
        entry.object = RegistryObject.create(id, registryKey, namespace);
        entries.add(entry);
        return entry;
    }

    @Override
    public <T extends Item> PortDeferredItem<T> register(String name, Function<ResourceLocation, T> valueFunction) {
        ResourceLocation id = asId(name);
        PortDeferredItem<T> entry = new PortDeferredItem<>(id, () -> valueFunction.apply(id));
        entry.object = RegistryObject.create(id, registryKey, namespace);
        entries.add(entry);
        return entry;
    }

    public PortDeferredItem<Item> registerSimpleItem(String name, Item.Properties properties) {
        return register(name, () -> new Item(properties));
    }

    public PortDeferredItem<BlockItem> registerSimpleBlockItem(PortDeferredBlock<? extends Block> block) {
        return registerSimpleBlockItem(block, new Item.Properties());
    }

    public PortDeferredItem<BlockItem> registerSimpleBlockItem(PortDeferredBlock<? extends Block> block, Item.Properties properties) {
        return register(block.getId().getPath(), () -> new BlockItem(block.get(), properties));
    }

    public PortDeferredItem<Item> registerItem(String name, Item.Properties properties) {
        return registerItem(name, Item::new);
    }

    public <T extends Item> PortDeferredItem<T> registerItem(String name, Function<Item.Properties, T> function) {
        return register(name, () -> function.apply(new Item.Properties()));
    }
}
