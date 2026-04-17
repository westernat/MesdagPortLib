package org.mesdag.portlib.diff.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.mesdag.portlib.event.PortEventHandler;
import org.mesdag.portlib.event.level.PortExplosionKnockbackEvent;
import org.mesdag.portlib.wrapper.PortSelfGetter;
import org.mesdag.portlib.wrapper.world.level.PortExplosionDamageCalculator;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Explosion.class)
public abstract class ExplosionMixin implements PortSelfGetter<Explosion> {
    @Shadow
    @Final
    private Level level;

    @Shadow
    @Final
    private ExplosionDamageCalculator damageCalculator;

    @ModifyVariable(method = "explode", at = @At(value = "STORE"), name = "vec31")
    private Vec3 getExplosionKnockback(Vec3 original, @Local(name = "entity") Entity entity) {
        PortExplosionKnockbackEvent event = new PortExplosionKnockbackEvent(level, portlib$self(), entity, original);
        PortEventHandler.postEvent(event);
        return event.getKnockbackVelocity();
    }

    @WrapOperation(method = "explode", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
    private boolean wrapHurt(Entity instance, DamageSource source, float amount, Operation<Boolean> original) {
        if (damageCalculator instanceof PortExplosionDamageCalculator port && port.shouldDamageEntity(portlib$self(), instance)) {
            float damage = port.getEntityDamage(portlib$self(), instance);
            if (damage < 0) {
                damage = amount;
            }
            return original.call(instance, source, port.modifyEntityDamage(portlib$self(), instance, damage));
        }
        return original.call(instance, source, amount);
    }

    @ModifyArg(method = "explode", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/ProtectionEnchantment;getExplosionKnockbackAfterDampener(Lnet/minecraft/world/entity/LivingEntity;D)D"))
    private double applyKnockbackMultiplier1(double damage, @Local(name = "entity") Entity entity) {
        if (damageCalculator instanceof PortExplosionDamageCalculator port) {
            return damage * port.getKnockbackMultiplier(entity);
        }
        return damage;
    }

    @ModifyVariable(method = "explode", at = @At(value = "STORE", ordinal = 1), name = "d11")
    private double applyKnockbackMultiplier2(double original, @Local(name = "entity") Entity entity) {
        if (damageCalculator instanceof PortExplosionDamageCalculator port) {
            return original * port.getKnockbackMultiplier(entity);
        }
        return original;
    }
}
