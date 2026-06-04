package org.mesdag.portlib.diff.mixin;

import net.minecraft.world.effect.MobEffect;
import org.mesdag.portlib.diff.IPortMobEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MobEffect.class)
public abstract class MobEffectMixin implements IPortMobEffect {
    @Shadow
    private Object effectRenderer;

    @Override
    public void portlib$setRenderPropertiesInternal(Object properties) {
        this.effectRenderer = properties;
    }
}
