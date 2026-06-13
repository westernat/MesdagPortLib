package org.mesdag.portlib.diff.mixin;

import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Date;

@org.spongepowered.asm.mixin.Mixin(net.minecraft.advancements.CriterionProgress.class)
public interface CriterionProgressAccessor {
    @Accessor
    void setObtained(Date obtained);
}
