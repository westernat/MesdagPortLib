package PortLib.extensions.net.minecraft.core.RegistryAccess;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;

import java.util.stream.Stream;

@Extension
public class PortRegistryAccessExtension {
    public static Stream<ResourceKey<? extends Registry<?>>> listRegistries(@This RegistryAccess thiz) {
        return thiz.registries().map(RegistryAccess.RegistryEntry::key);
    }
}
