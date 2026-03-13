package org.mesdag.portlib.wrapper.core;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import org.mesdag.portlib.diff.Diff;

public class PortRegistry<V> {
    private final IForgeRegistry<V> registry;

    private PortRegistry(IForgeRegistry<V> registry) {
        this.registry = registry;
    }

    public void register(ResourceLocation key, V value) {
        registry.register(key, value);
    }

    @Diff
    public static <V> PortRegistry<V> wrap(IForgeRegistry<V> registry) {
        return new PortRegistry<>(registry);
    }
}
