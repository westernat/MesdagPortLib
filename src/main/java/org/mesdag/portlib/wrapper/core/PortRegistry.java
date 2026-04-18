package org.mesdag.portlib.wrapper.core;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.mesdag.portlib.diff.Diff;

public class PortRegistry<V> {
    private final Registry<V> registry;

    @Diff
    public PortRegistry(Registry<V> registry) {
        this.registry = registry;
    }

    public void register(ResourceLocation key, V value) {
        Registry.register(registry, key, value);
    }

    public ResourceKey<? extends Registry<V>> key() {
        return registry.key();
    }

    @Diff
    public Registry<V> unwrap() {
        return registry;
    }
}
