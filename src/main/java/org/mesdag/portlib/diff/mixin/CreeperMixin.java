package org.mesdag.portlib.diff.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Creeper;
import org.mesdag.portlib.wrapper.common.extensions.IPortLivingEntityExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Creeper.class)
public abstract class CreeperMixin implements IPortLivingEntityExtension {
    @Inject(method = "explodeCreeper", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/Creeper;discard()V"))
    private void callTriggerOnDeathMobEffects(CallbackInfo ci) {
        triggerOnDeathMobEffects(Entity.RemovalReason.KILLED);
    }
}
