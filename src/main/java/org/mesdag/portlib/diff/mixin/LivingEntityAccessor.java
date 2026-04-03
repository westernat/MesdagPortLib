package org.mesdag.portlib.diff.mixin;

import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.gen.Invoker;

@org.spongepowered.asm.mixin.Mixin(net.minecraft.world.entity.LivingEntity.class)
public interface LivingEntityAccessor {
    @Invoker
    void callOnEffectRemoved(MobEffectInstance effectInstance);
}
