package org.mesdag.portlib.diff.mixin;

import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySynchronization;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@org.spongepowered.asm.mixin.Mixin(value = net.minecraftforge.registries.DataPackRegistriesHooks.class, remap = false)
public interface DataPackRegistriesHooksAccessor {
    @Accessor
    static Map<ResourceKey<? extends Registry<?>>, RegistrySynchronization.NetworkedRegistryData<?>> getNETWORKABLE_REGISTRIES() {throw new UnsupportedOperationException();}
}
