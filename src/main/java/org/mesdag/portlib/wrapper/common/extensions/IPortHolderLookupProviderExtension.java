package org.mesdag.portlib.wrapper.common.extensions;

import PortLib.extensions.net.minecraft.resources.ResourceKey.PortResourceKeyExtension;
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
        return RegistryOps.create(ops, self());
    }

    default <T> Holder<T> holderOrThrow(ResourceKey<T> key) {
        return self().lookupOrThrow(PortResourceKeyExtension.registryKey(key)).getOrThrow(key);
    }

    default <T> Optional<Holder.Reference<T>> holder(ResourceKey<T> key) {
        Optional<HolderLookup.RegistryLookup<T>> registry = self().lookup(PortResourceKeyExtension.registryKey(key));
        return registry.flatMap(tRegistryLookup -> tRegistryLookup.get(key));
    }

    static IPortHolderLookupProviderExtension of(HolderLookup.Provider provider) {
        return (IPortHolderLookupProviderExtension) provider;
    }
}
