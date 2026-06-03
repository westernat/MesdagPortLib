package org.mesdag.portlib.diff.mixin;

import com.google.common.collect.BiMap;
import org.spongepowered.asm.mixin.gen.Accessor;

@org.spongepowered.asm.mixin.Mixin(value = net.minecraftforge.registries.ForgeRegistry.class, remap = false)
public interface ForgeRegistryAccessor<V> {
    @Accessor
    BiMap<Integer, V> getIds();
}
