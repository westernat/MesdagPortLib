package PortLib.extensions.net.minecraft.core.HolderLookup;

import com.mojang.serialization.DynamicOps;
import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.RegistryOps;

@Extension
public class PortHolderLookupExtension {
    public static class Provider {
        public static <V> RegistryOps<V> createSerializationContext(@This HolderLookup.Provider thiz, DynamicOps<V> ops) {
            return RegistryOps.create(ops, thiz);
        }
    }
}
