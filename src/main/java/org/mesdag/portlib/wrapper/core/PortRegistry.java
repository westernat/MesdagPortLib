package org.mesdag.portlib.wrapper.core;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import org.mesdag.portlib.diff.Diff;

public class PortRegistry<V> {
    private final Registry<V> registry;

    private PortRegistry(Registry<V> registry) {
        this.registry = registry;
    }

    public void register(ResourceLocation key, V value) {
        Registry.register(registry, key, value);
    }

    @Diff
    public static <V> PortRegistry<V> wrap(Registry<V> registry) {
        return new PortRegistry<>(registry);
    }
}
