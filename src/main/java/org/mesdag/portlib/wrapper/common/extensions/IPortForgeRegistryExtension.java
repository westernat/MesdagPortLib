package org.mesdag.portlib.wrapper.common.extensions;

import net.minecraft.resources.ResourceKey;
import net.minecraftforge.registries.IForgeRegistry;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.datamap.PortDataMapType;
import org.mesdag.portlib.diff.datamap.PortDataMapLoader;

// implements by coremod
public interface IPortForgeRegistryExtension<V> {
    @SuppressWarnings("unchecked")
    default <A> @Nullable A getData(PortDataMapType<V, A> type, ResourceKey<V> key) {
        return PortDataMapLoader.getInstance().getData(((IForgeRegistry<V>) this).getRegistryKey(), type, key);
    }
}
