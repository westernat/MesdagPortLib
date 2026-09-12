package org.mesdag.portlib.diff.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = CapabilityProvider.class, remap = false)
public interface CapabilityProviderAccessor {
    @Invoker(remap = false)
    CompoundTag callSerializeCaps();
}
