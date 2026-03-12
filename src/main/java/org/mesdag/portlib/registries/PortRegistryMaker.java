package org.mesdag.portlib.registries;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegistryBuilder;
import org.mesdag.portlib.diff.Diff;

public class PortRegistryMaker<T> {
    private final RegistryBuilder<T> builder;
    private boolean sync = false;

    @Diff
    public PortRegistryMaker() {
        this.builder = new RegistryBuilder<>();
    }

    public PortRegistryMaker<T> defaultKey(ResourceLocation key) {
        builder.setDefaultKey(key);
        return this;
    }

    public PortRegistryMaker<T> defaultKey(ResourceKey<T> key) {
        return defaultKey(key.location());
    }

    public PortRegistryMaker<T> maxId(int max) {
        builder.setMaxID(max);
        return this;
    }

    public PortRegistryMaker<T> sync(boolean sync) {
        this.sync = sync;
        return this;
    }

    @Diff
    public RegistryBuilder<T> make() {
        if (!sync) {
            builder.disableSync();
        }
        return builder;
    }
}
