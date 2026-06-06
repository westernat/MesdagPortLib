package org.mesdag.portlib.diff.mixin;

import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Optional;

@org.spongepowered.asm.mixin.Mixin(value = com.mojang.serialization.DataResult.PartialResult.class, remap = false)
public interface PartialResultAccessor<R> {
    @Accessor
    Optional<R> getPartialResult();
}
