package org.mesdag.portlib.diff.mixin;

import net.minecraft.world.effect.MobEffect;
import org.mesdag.portlib.wrapper.common.extensions.IPortMobEffectExtension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MobEffect.class)
public class MobEffectMixin implements IPortMobEffectExtension {
}
