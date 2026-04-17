package PortLib.extensions.net.minecraft.core.HolderLookup;

import com.mojang.serialization.DynamicOps;
import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;

import java.util.Optional;

@Extension
public class PortHolderLookupExtension {
    public static class Provider {
        public static <V> RegistryOps<V> createSerializationContext(@This HolderLookup.Provider thiz, DynamicOps<V> ops) {
            return RegistryOps.create(ops, thiz);
        }

        public static <T> Holder<T> holderOrThrow(@This HolderLookup.Provider thiz, ResourceKey<T> key) {
            return thiz.lookupOrThrow(key.registryKey()).getOrThrow(key);
        }

        public static <T> Optional<Holder.Reference<T>> holder(@This HolderLookup.Provider thiz, ResourceKey<T> key) {
            Optional<HolderLookup.RegistryLookup<T>> registry = thiz.lookup(key.registryKey());
            return registry.flatMap(tRegistryLookup -> tRegistryLookup.get(key));
        }
    }
}
