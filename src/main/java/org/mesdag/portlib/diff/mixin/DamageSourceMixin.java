package org.mesdag.portlib.diff.mixin;

import net.minecraft.world.damagesource.DamageSource;
import org.mesdag.portlib.wrapper.common.extensions.IPortDamageSourceExtension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DamageSource.class)
public abstract class DamageSourceMixin implements IPortDamageSourceExtension {
}
