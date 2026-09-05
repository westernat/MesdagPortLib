package org.mesdag.portlib.diff.mixin;

import net.minecraft.advancements.AdvancementProgress;
import org.mesdag.portlib.wrapper.common.extensions.IPortAdvancementProgressExtension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AdvancementProgress.class)
public abstract class AdvancementProgressMixin implements IPortAdvancementProgressExtension {
}
