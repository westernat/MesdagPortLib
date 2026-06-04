package PortLib.extensions.net.minecraft.core.RegistryAccess;

import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;

import java.util.stream.Stream;

public class PortRegistryAccessExtension {
    public static Stream<ResourceKey<? extends Registry<?>>> listRegistries(RegistryAccess thiz) {
        return thiz.registries().map(RegistryAccess.RegistryEntry::key);
    }
}
