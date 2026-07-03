package org.mesdag.portlib.wrapper.common.extensions;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.datamap.PortDataMapType;
import org.mesdag.portlib.diff.datamap.PortDataMapLoader;

// implements by coremod
public interface IPortRegistryExtension<T> {
    @SuppressWarnings("unchecked")
    default <A> @Nullable A getData(PortDataMapType<T, A> type, ResourceKey<T> key) {
        return PortDataMapLoader.getInstance().getData(((Registry<T>) this).key(), type, key);
    }
}
