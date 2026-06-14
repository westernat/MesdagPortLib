package org.mesdag.portlib.registries;

import com.google.common.base.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Function;

public class PortBlockRegistration extends PortRegistration<Block> {
    PortBlockRegistration(String namespace) {
        super(namespace, Registries.BLOCK);
    }

    @Override
    public <T extends Block> PortDeferredBlock<T> register(String name, Supplier<T> valueSupplier) {
        ResourceLocation id = asId(name);
        PortDeferredBlock<T> entry = new PortDeferredBlock<>(id, valueSupplier);
        entry.object = RegistryObject.create(id, registryKey, namespace);
        entries.add(entry);
        return entry;
    }

    @Override
    public <T extends Block> PortDeferredBlock<T> register(String name, Function<ResourceLocation, T> valueFunction) {
        ResourceLocation id = asId(name);
        PortDeferredBlock<T> entry = new PortDeferredBlock<>(id, () -> valueFunction.apply(id));
        entry.object = RegistryObject.create(id, registryKey, namespace);
        entries.add(entry);
        return entry;
    }
}
