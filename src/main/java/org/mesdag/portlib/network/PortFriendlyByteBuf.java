package org.mesdag.portlib.network;

import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class PortFriendlyByteBuf {
    public static <T> ResourceKey<? extends Registry<T>> readRegistryKey(FriendlyByteBuf buf) {
        ResourceLocation resourcelocation = buf.readResourceLocation();
        return ResourceKey.createRegistryKey(resourcelocation);
    }
}
