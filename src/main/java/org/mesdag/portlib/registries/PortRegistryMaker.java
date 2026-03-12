package org.mesdag.portlib.registries;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.RegistryBuilder;
import org.mesdag.portlib.diff.Diff;

public class PortRegistryMaker<T> {
    private final RegistryBuilder<T> builder;

    @Diff
    public PortRegistryMaker(RegistryBuilder<T> builder) {
        this.builder = builder;
    }

    public PortRegistryMaker<T> defaultKey(ResourceLocation key) {
        builder.defaultKey(key);
        return this;
    }

    public PortRegistryMaker<T> defaultKey(ResourceKey<T> key) {
        builder.defaultKey(key);
        return this;
    }

    public PortRegistryMaker<T> maxId(int max) {
        builder.maxId(max);
        return this;
    }

    public PortRegistryMaker<T> sync(boolean sync) {
        builder.sync(sync);
        return this;
    }
}
