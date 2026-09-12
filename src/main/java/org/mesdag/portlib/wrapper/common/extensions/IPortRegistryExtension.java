package org.mesdag.portlib.wrapper.common.extensions;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.datamap.PortDataMapType;
import org.mesdag.portlib.diff.datamap.PortDataMapLoader;

import java.util.Map;

// implements by coremod
public interface IPortRegistryExtension<T> {
    @SuppressWarnings("unchecked")
    private Registry<T> self() {
        return (Registry<T>) this;
    }

    default <A> @Nullable A getData(PortDataMapType<T, A> type, ResourceKey<T> key) {
        return PortDataMapLoader.getInstance().getData(self().key(), type, key);
    }

    default <A> Map<ResourceKey<T>, A> getDataMap(PortDataMapType<T, A> type) {
        return PortDataMapLoader.getInstance().getDataMap(self().key(), type);
    }
}
