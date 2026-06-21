package org.mesdag.portlib.wrapper.common.extensions;

import PortLib.extensions.net.minecraft.core.HolderLookup.PortHolderLookupExtension;
import com.mojang.serialization.DynamicOps;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;

import java.util.Optional;

@SuppressWarnings("all")
public interface IPortHolderLookupProviderExtension {

    private HolderLookup.Provider self() {
        return (HolderLookup.Provider) this;
    }

    default <V> RegistryOps<V> createSerializationContext(DynamicOps<V> ops) {
        return PortHolderLookupExtension.Provider.createSerializationContext(self(), ops);
    }

    default <T> Holder<T> holderOrThrow(ResourceKey<T> key) {
        return PortHolderLookupExtension.Provider.holderOrThrow(self(), key);
    }

    default <T> Optional<Holder.Reference<T>> holder(ResourceKey<T> key) {
        return PortHolderLookupExtension.Provider.holder(self(), key);
    }

    static IPortHolderLookupProviderExtension of(HolderLookup.Provider provider) {
        return (IPortHolderLookupProviderExtension) provider;
    }
}
