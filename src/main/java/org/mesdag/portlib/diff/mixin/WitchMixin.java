package org.mesdag.portlib.diff.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import net.minecraft.world.entity.monster.Witch;
import org.mesdag.portlib.diff.IPortLivingEntity;
import org.mesdag.portlib.wrapper.PortSelfGetter;
import org.mesdag.portlib.wrapper.common.damagesource.PortDamageContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Witch.class)
public abstract class WitchMixin implements PortSelfGetter<Witch> {
    @Inject(method = "getDamageAfterMagicAbsorb", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/damagesource/DamageSource;getEntity()Lnet/minecraft/world/entity/Entity;"))
    private void cacheDamage(CallbackInfoReturnable<Float> ci, @Local(argsOnly = true) float damage, @Share("cachedDamage") LocalFloatRef cachedDamage) {
        cachedDamage.set(damage);
    }

    @Inject(method = "getDamageAfterMagicAbsorb", at = @At("TAIL"))
    private void setReduction(CallbackInfoReturnable<Float> cir, @Local(argsOnly = true) float damage, @Share("cachedDamage") LocalFloatRef cachedDamage) {
        float delta = cachedDamage.get() - damage;
        if (delta != 0) {
            PortDamageContainer container = IPortLivingEntity.of(portlib$self()).portlib$getDamageContainers().peek();
            container.setReduction(PortDamageContainer.PortReduction.INNATE_RESISTANCE, delta);
        }
    }
}
