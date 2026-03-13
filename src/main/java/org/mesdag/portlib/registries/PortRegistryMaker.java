package org.mesdag.portlib.registries;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.RegistryBuilder;
import org.mesdag.portlib.registries.callback.PortAddCallback;
import org.mesdag.portlib.registries.callback.PortBakeCallback;
import org.mesdag.portlib.registries.callback.PortClearCallback;
import org.mesdag.portlib.registries.callback.PortRegistryCallback;
import org.mesdag.portlib.wrapper.core.PortRegistry;

@SuppressWarnings("all")
public class PortRegistryMaker<T> {
    RegistryBuilder<T> builder;

    public PortRegistryMaker<T> defaultKey(ResourceLocation key) {
        builder.defaultKey(key);
        return this;
    }

    public PortRegistryMaker<T> defaultKey(ResourceKey<T> key) {
        builder.defaultKey(key);
        return this;
    }

    public PortRegistryMaker<T> callback(PortRegistryCallback<T> inst) {
        if (inst instanceof PortAddCallback<T> callback) {
            builder.onAdd((registry, id, key, value) -> callback.onAdd(PortRegistry.wrap(registry), id, key, value));
        }
        if (inst instanceof PortBakeCallback<T> callback) {
            builder.onBake(registry -> callback.onBake(PortRegistry.wrap(registry)));
        }
        if (inst instanceof PortClearCallback<T> callback) {
            builder.onClear((registry, full) -> callback.onClear(PortRegistry.wrap(registry)));
        }
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
