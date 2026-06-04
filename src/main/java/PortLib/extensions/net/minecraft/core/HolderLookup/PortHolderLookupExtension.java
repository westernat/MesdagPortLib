package PortLib.extensions.net.minecraft.core.HolderLookup;

import PortLib.extensions.net.minecraft.resources.ResourceKey.PortResourceKeyExtension;
import com.mojang.serialization.DynamicOps;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;

import java.util.Optional;

public class PortHolderLookupExtension {
    public static class Provider {
        public static <V> RegistryOps<V> createSerializationContext(HolderLookup.Provider thiz, DynamicOps<V> ops) {
            return RegistryOps.create(ops, thiz);
        }

        public static <T> Holder<T> holderOrThrow(HolderLookup.Provider thiz, ResourceKey<T> key) {
            return thiz.lookupOrThrow(PortResourceKeyExtension.registryKey(key)).getOrThrow(key);
        }

        public static <T> Optional<Holder.Reference<T>> holder(HolderLookup.Provider thiz, ResourceKey<T> key) {
            Optional<HolderLookup.RegistryLookup<T>> registry = thiz.lookup(PortResourceKeyExtension.registryKey(key));
            return registry.flatMap(tRegistryLookup -> tRegistryLookup.get(key));
        }
    }
}
