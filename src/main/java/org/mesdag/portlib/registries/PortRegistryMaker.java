package org.mesdag.portlib.registries;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegistryBuilder;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.registries.callback.PortAddCallback;
import org.mesdag.portlib.registries.callback.PortBakeCallback;
import org.mesdag.portlib.registries.callback.PortClearCallback;
import org.mesdag.portlib.registries.callback.PortRegistryCallback;
import org.mesdag.portlib.wrapper.core.PortRegistry;

@SuppressWarnings("all")
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

    public PortRegistryMaker<T> callback(PortRegistryCallback<T> inst) {
        if (inst instanceof PortAddCallback<T> callback) {
            builder.onAdd((owner, stage, id, key, obj, oldObj) -> callback.onAdd((PortRegistry<T>) owner.wrap(), id, key, obj));
        }
        if (inst instanceof PortBakeCallback<T> callback) {
            builder.onBake((owner, stage) -> callback.onBake((PortRegistry<T>) owner.wrap()));
        }
        if (inst instanceof PortClearCallback<T> callback) {
            builder.onClear((owner, stage) -> callback.onClear((PortRegistry<T>) owner.wrap()));
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
