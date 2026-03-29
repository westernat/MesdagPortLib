package org.mesdag.portlib.registries;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.RegistryBuilder;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.registries.callback.PortAddCallback;
import org.mesdag.portlib.registries.callback.PortBakeCallback;
import org.mesdag.portlib.registries.callback.PortClearCallback;
import org.mesdag.portlib.registries.callback.PortRegistryCallback;

@SuppressWarnings("all")
public class PortRegistryMaker<T> {
    @Diff
    public RegistryBuilder<T> builder;

    public PortRegistryMaker<T> defaultKey(ResourceLocation key) {
        builder.defaultKey(key);
        return this;
    }

    public PortRegistryMaker<T> defaultKey(ResourceKey<T> key) {
        builder.defaultKey(key);
        return this;
    }

    public PortRegistryMaker<T> callback(PortRegistryCallback<T> inst) {
        builder.callback(inst.unwrap());
        return this;
    }

    public PortRegistryMaker<T> onAdd(PortAddCallback<T> callback) {
        return callback(callback);
    }

    public PortRegistryMaker<T> onBake(PortBakeCallback<T> callback) {
        return callback(callback);
    }

    public PortRegistryMaker<T> onClear(PortClearCallback<T> callback) {
        return callback(callback);
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
