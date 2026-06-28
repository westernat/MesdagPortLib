package org.mesdag.portlib.wrapper.common.extensions;

import PortLib.extensions.net.minecraft.core.Holder.PortHolderExtension;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.datamap.PortDataMapType;

/// implemented by coremod
@SuppressWarnings("unchecked")
public interface IPortHolderExtension<T> {
    private Holder<T> self() {
        return (Holder<T>) this;
    }

    default boolean is(Holder<T> holder) {
        return PortHolderExtension.is(self(), holder);
    }

    default String getRegisteredName() {
        return PortHolderExtension.getRegisteredName(self());
    }

    default @Nullable ResourceKey<T> getKey() {
        return PortHolderExtension.getKey(self());
    }

    default HolderLookup.@Nullable RegistryLookup<T> unwrapLookup() {
        return PortHolderExtension.unwrapLookup(self());
    }

    default <A> @Nullable A getData(PortDataMapType<T, A> type) {
        return PortHolderExtension.getData(self(), type);
    }

    static <T> IPortHolderExtension<T> of(Holder<T> holder) {
        return (IPortHolderExtension<T>) holder;
    }
}
