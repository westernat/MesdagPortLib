package org.mesdag.portlib.diff.mixin;

import PortLib.extensions.net.minecraft.world.entity.LivingEntity.PortLivingEntityExtension;
import PortLib.extensions.net.minecraft.world.entity.ai.attributes.Attributes.PortAttributesExtension;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.PotionColorCalculationEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.IPortLivingEntity;
import org.mesdag.portlib.event.PortEventHandler;
import org.mesdag.portlib.event.entity.living.*;
import org.mesdag.portlib.event.entity.player.PortCanContinueSleepingEvent;
import org.mesdag.portlib.wrapper.PortSelfGetter;
import org.mesdag.portlib.wrapper.common.PortEffectCures;
import org.mesdag.portlib.wrapper.common.damagesource.PortDamageContainer;
import org.mesdag.portlib.wrapper.common.extensions.IPortMobEffectInstanceExtension;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;
import java.util.Stack;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements IPortLivingEntity, PortSelfGetter<LivingEntity> {
    @Shadow
    protected float lastHurt;

    @Shadow
    public abstract float getAbsorptionAmount();

    @Shadow
    public abstract void setAbsorptionAmount(float absorptionAmount);

    @Shadow
    @Final
    private Map<MobEffect, MobEffectInstance> activeEffects;

    @Shadow
    public abstract double getAttributeValue(Holder<Attribute> attribute);

    @Shadow
    public abstract AttributeMap getAttributes();

    @Unique
    private Stack<PortDamageContainer> portlib$damageContainers = new Stack<>();
    @Unique
    private List<ParticleOptions> portlib$effectParticles = List.of();
    @Unique
    private boolean portlib$dirty = false;

    @Override
    public void portlib$setDamageContainers(Stack<PortDamageContainer> damageContainers) {
        this.portlib$damageContainers = damageContainers;
    }

    @Override
    public Stack<PortDamageContainer> portlib$getDamageContainers() {
        return portlib$damageContainers;
    }

    @Override
    public void portlib$setEffectParticles(List<ParticleOptions> list) {
        this.portlib$effectParticles = list;
    }

    @Override
    public List<ParticleOptions> portlib$getEffectParticles() {
        return portlib$effectParticles;
    }

    @Override
    public void portlib$setDirty(boolean dirty) {
        this.portlib$dirty = dirty;
    }

    @Override
    public boolean portlib$isDirty() {
        return portlib$dirty;
    }

    // region hurt

    @Inject(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isSleeping()Z"), cancellable = true)
    private void push(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        portlib$damageContainers.push(new PortDamageContainer(source, amount));
        if (PortEventHandler.postEventWithReturn(new PortLivingIncomingDamageEvent(portlib$self(), portlib$damageContainers.peek())).isCanceled()) {
            cir.setReturnValue(false);
        }
    }

    @ModifyVariable(method = "hurt", at = @At(value = "STORE"), name = "f")
    private float peekNewDamage(float f) {
        return portlib$damageContainers.peek().getNewDamage();
    }

    @ModifyExpressionValue(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isDamageSourceBlocked(Lnet/minecraft/world/damagesource/DamageSource;)Z"))
    private boolean onDamageBlock(boolean original, @Share("portlib$ev") LocalRef<PortLivingShieldBlockEvent> portlib$ev) {
        PortLivingShieldBlockEvent event = new PortLivingShieldBlockEvent(portlib$self(), portlib$damageContainers.peek(), original);
        portlib$ev.set(event);
        PortEventHandler.postEvent(event);
        return event.getBlocked();
    }

    @Inject(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/event/entity/living/ShieldBlockEvent;isCanceled()Z", remap = false))
    private void dealForgeEv(
            CallbackInfoReturnable<Boolean> cir,
            @Local(name = "ev") ShieldBlockEvent ev,
            @Share("portlib$ev") LocalRef<PortLivingShieldBlockEvent> portlib$ev
    ) {
        if (ev.isCanceled() && !portlib$ev.get().getOriginalBlock()) {
            ev.setCanceled(false);
        }
        if (!ev.shieldTakesDamage()) {
            portlib$ev.get().setShieldDamage(0);
        }
    }

    @ModifyArg(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;hurtCurrentlyUsedShield(F)V"))
    private float modifyShieldDamage(float original, @Share("portlib$ev") LocalRef<PortLivingShieldBlockEvent> portlib$ev) {
        return portlib$ev.get().shieldDamage();
    }

    @Inject(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/WalkAnimationState;setSpeed(F)V"))
    private void updateContainer(CallbackInfoReturnable<Boolean> cir, @Local(argsOnly = true) float amount) {
        portlib$damageContainers.peek().setNewDamage(amount);
    }

    @Inject(method = "hurt", at = @At(value = "RETURN", ordinal = 5))
    private void popEarly(CallbackInfoReturnable<Boolean> cir) {
        portlib$damageContainers.pop();
    }

    @Inject(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;actuallyHurt(Lnet/minecraft/world/damagesource/DamageSource;F)V", ordinal = 0))
    private void setReduction(CallbackInfoReturnable<Boolean> cir) {
        portlib$damageContainers.peek().setReduction(PortDamageContainer.PortReduction.INVULNERABILITY, lastHurt);
    }

    @Inject(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;actuallyHurt(Lnet/minecraft/world/damagesource/DamageSource;F)V", ordinal = 1))
    private void setInvulnerableTime(CallbackInfoReturnable<Boolean> cir) {
        portlib$self().invulnerableTime = portlib$damageContainers.peek().getPostAttackInvulnerabilityTicks();
    }

    @ModifyVariable(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/damagesource/DamageSource;is(Lnet/minecraft/tags/TagKey;)Z", ordinal = 4), argsOnly = true)
    private float updateLocalWithContainerValue(float original) {
        return portlib$damageContainers.peek().getNewDamage();
    }

    @Inject(method = "hurt", at = @At(value = "RETURN", ordinal = 6))
    private void popLast(CallbackInfoReturnable<Boolean> cir) {
        portlib$damageContainers.pop();
    }

    // endregion hurt

    @ModifyVariable(method = "getDamageAfterMagicAbsorb", at = @At(value = "STORE", ordinal = 1), argsOnly = true)
    private float setEnchantmentsReduction(float original) {
        portlib$damageContainers.peek().setReduction(PortDamageContainer.PortReduction.ENCHANTMENTS, portlib$damageContainers.peek().getNewDamage() - original);
        return original;
    }

    // region actuallyHurt

    /// ```java
    /// damageAmount = this.getDamageAfterArmorAbsorb(damageSource, damageAmount);
    ///```
    /// ↓
    /// ```java
    /// this.damageContainers.peek().setReduction(net.neoforged.neoforge.common.damagesource.DamageContainer.Reduction.ARMOR, this.damageContainers.peek().getNewDamage() - this.getDamageAfterArmorAbsorb(damageSrc, this.damageContainers.peek().getNewDamage()));
    /// this.getDamageAfterMagicAbsorb(damageSrc, this.damageContainers.peek().getNewDamage());
    ///```
    @WrapOperation(method = "actuallyHurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getDamageAfterArmorAbsorb(Lnet/minecraft/world/damagesource/DamageSource;F)F"))
    private float setArmorReduction(LivingEntity instance, DamageSource damageSource, float damageAmount, Operation<Float> original) {
        PortDamageContainer container = portlib$damageContainers.peek();
        container.setReduction(PortDamageContainer.PortReduction.ARMOR, container.getNewDamage() - original.call(instance, damageSource, container.getNewDamage()));
        return container.getNewDamage(); // 即onLivingDamagePre方法替换掉的getDamageAfterMagicAbsorb方法的第二个参数
    }

    /// ```java
    /// damageAmount = this.getDamageAfterMagicAbsorb(damageSource, damageAmount);
    ///```
    /// ↓
    /// ```java
    /// float damage = net.neoforged.neoforge.common.CommonHooks.onLivingDamagePre(this, this.damageContainers.peek());
    /// this.damageContainers.peek().setReduction(net.neoforged.neoforge.common.damagesource.DamageContainer.Reduction.ABSORPTION, Math.min(this.getAbsorptionAmount(), damage));
    /// float absorbed = Math.min(damage, this.damageContainers.peek().getReduction(net.neoforged.neoforge.common.damagesource.DamageContainer.Reduction.ABSORPTION));
    /// this.setAbsorptionAmount(Math.max(0, this.getAbsorptionAmount() - absorbed));
    ///```
    @ModifyExpressionValue(method = "actuallyHurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getDamageAfterMagicAbsorb(Lnet/minecraft/world/damagesource/DamageSource;F)F"))
    private float onLivingDamagePre(float ignored, @Share("absorbed") LocalFloatRef absorbed) {
        PortDamageContainer container = portlib$damageContainers.peek();
        float damage = PortLivingDamageEvent.Pre.onLivingDamagePre(portlib$self(), container);
        container.setReduction(PortDamageContainer.PortReduction.ABSORPTION, Math.min(getAbsorptionAmount(), damage));
        absorbed.set(Math.min(damage, container.getReduction(PortDamageContainer.PortReduction.ABSORPTION)));
        setAbsorptionAmount(Math.max(0, getAbsorptionAmount() - absorbed.get()));
        return ignored;
    }

    /// ```java
    /// float f1 = Math.max(damageAmount - this.getAbsorptionAmount(), 0.0F);
    ///```
    /// ↓
    /// ```java
    /// float f1 = this.damageContainers.peek().getNewDamage();
    ///```
    @ModifyVariable(method = "actuallyHurt", at = @At(value = "STORE", ordinal = 0), name = "f1")
    private float modifyF1(float f1) {
        return portlib$damageContainers.peek().getNewDamage();
    }

    @WrapWithCondition(method = "actuallyHurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;setAbsorptionAmount(F)V"))
    private boolean deny(LivingEntity instance, float absorptionAmount) {
        return false;
    }

    /// ```java
    /// float f = damageAmount - f1;
    ///```
    /// ↓
    /// ```java
    /// float f = absorbed;
    ///```
    @ModifyVariable(method = "actuallyHurt", at = @At(value = "STORE", ordinal = 0), name = "f")
    private float modifyF(float f, @Share("absorbed") LocalFloatRef absorbed) {
        return absorbed.get();
    }

    @Inject(method = "actuallyHurt", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/common/ForgeHooks;onLivingHurt(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/damagesource/DamageSource;F)F", remap = false))
    private void markCouldPost(CallbackInfo ci, @Share("couldPost") LocalBooleanRef couldPost) {
        couldPost.set(true);
    }

    @Inject(method = "actuallyHurt", at = @At("RETURN"))
    private void onLivingDamagePost(CallbackInfo ci, @Share("couldPost") LocalBooleanRef couldPost) {
        if (couldPost.get()) {
            PortDamageContainer container = IPortLivingEntity.of(portlib$self()).portlib$getDamageContainers().peek();
            PortLivingDamageEvent.Post.onLivingDamagePost(portlib$self(), container);
        }
    }

    @Inject(method = "actuallyHurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;gameEvent(Lnet/minecraft/world/level/gameevent/GameEvent;)V", shift = At.Shift.AFTER))
    private void onDamageTaken(CallbackInfo ci) {
        PortLivingEntityExtension.onDamageTaken(portlib$self(), portlib$damageContainers.peek());
    }

    // endregion actuallyHurt

    @ModifyExpressionValue(method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;canBeAffected(Lnet/minecraft/world/effect/MobEffectInstance;)Z"))
    private boolean canMobEffectBeApplied1(boolean original, @Local(argsOnly = true) MobEffectInstance instance, @Local(argsOnly = true) @Nullable Entity entity) {
        return original && PortMobEffectEvent.Applicable.canMobEffectBeApplied(portlib$self(), instance, entity);
    }

    @ModifyExpressionValue(method = "forceAddEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;canBeAffected(Lnet/minecraft/world/effect/MobEffectInstance;)Z"))
    private boolean canMobEffectBeApplied2(boolean original, @Local(argsOnly = true) MobEffectInstance instance, @Local(argsOnly = true) @Nullable Entity entity) {
        return original && PortMobEffectEvent.Applicable.canMobEffectBeApplied(portlib$self(), instance, entity);
    }

    @WrapOperation(method = "checkTotemDeathProtection", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;removeAllEffects()Z"))
    private boolean replace(LivingEntity instance, Operation<Boolean> original) {
        return PortLivingEntityExtension.removeEffectsCuredBy(instance, PortEffectCures.PROTECTED_BY_TOTEM);
    }

    @ModifyReturnValue(method = "checkBedExists", at = @At("RETURN"))
    private boolean canEntityContinueSleeping(boolean original) {
        return PortCanContinueSleepingEvent.canEntityContinueSleeping(portlib$self(), original ? null : Player.BedSleepingProblem.NOT_POSSIBLE_HERE);
    }

    // region particles

    @Inject(method = "updateInvisibilityStatus", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;setInvisible(Z)V", ordinal = 0))
    private void updateSynchronizedMobEffectParticles(CallbackInfo ci) {
        this.portlib$effectParticles = List.of();
        this.portlib$dirty = true;
    }

    @Inject(method = "updateInvisibilityStatus", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/syncher/SynchedEntityData;set(Lnet/minecraft/network/syncher/EntityDataAccessor;Ljava/lang/Object;)V", ordinal = 0))
    private void updateSynchronizedMobEffectParticles(CallbackInfo ci, @Local(name = "event") PotionColorCalculationEvent event) { // serverside, LivingEntity.effectsDirty
        this.portlib$effectParticles = activeEffects.values().stream()
                .map(instance -> PortEventHandler.postEventWithReturn(new PortEffectParticleModificationEvent(portlib$self(), instance)))
                .filter(PortEffectParticleModificationEvent::isCustomParticle)
                .map(PortEffectParticleModificationEvent::getParticleOptions)
                .toList();
        if (activeEffects.size() == portlib$effectParticles.size()) {
            event.setColor(0);
            event.shouldHideParticles(false);
        }
        this.portlib$dirty = true;
    }

    @Inject(method = "tickEffects", at = @At("TAIL"))
    private void tickEffectParticles(CallbackInfo ci, @Local(name = "flag1") boolean flag1) { // clientside
        LivingEntity self = portlib$self();
        if (self.level().isClientSide && !portlib$effectParticles.isEmpty()) {
            int invisibleFactor = self.isInvisible() ? 15 : 4;
            int ambienceFactor = flag1 ? 5 : 1;
            if (self.getRandom().nextInt(invisibleFactor * ambienceFactor) == 0) {
                ParticleOptions particle = Util.getRandom(portlib$effectParticles, self.getRandom());
                self.level().addParticle(particle, self.getRandomX(0.5), self.getRandomY(), self.getRandomZ(0.5), 1.0, 1.0, 1.0);
            }
        }
    }

    // endregion particles

    // region attributes

    /// 把 1.21 的连续水中移动效率换算为 1.20 水中移动公式使用的三档系数。
    ///
    /// 这里修改刚写入的浮点局部变量，而不是把效率乘到整数附魔等级上。这样没有
    /// 深海探索者时效率仍会生效，0 到 1 的小数值也不会因整数截断而丢失。
    @ModifyVariable(method = "travel", at = @At(value = "STORE", ordinal = 0), name = "f6")
    private float applyWaterMovementEfficiency(float f6) {
        float efficiency = (float) getAttributeValue(PortAttributesExtension.waterMovementEfficiency());
        return Math.max(f6, efficiency * 3.0F);
    }

    @ModifyReturnValue(method = "getBlockSpeedFactor", at = @At("RETURN"))
    private float applyMovementEfficiency(float original) {
        return Mth.lerp((float) getAttributeValue(PortAttributesExtension.movementEfficiency()), original, 1.0F);
    }

    @ModifyArg(method = "causeFallDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"), index = 1)
    private float applyFallDamageMultiplier(float damage) {
        return (float) (damage * getAttributeValue(PortAttributesExtension.fallDamageMultiplier()));
    }

    @ModifyExpressionValue(method = "calculateFallDamage", at = @At(value = "CONSTANT", args = "floatValue=3.0"))
    private float applySafeFallDistance(float original) {
        return (float) getAttributeValue(PortAttributesExtension.safeFallDistance());
    }

    @ModifyReturnValue(method = "increaseAirSupply", at = @At("RETURN"))
    private int applyOxygenBonus(int original) {
        return Math.min(original + (int) getAttributeValue(PortAttributesExtension.oxygenBonus()), portlib$self().getMaxAirSupply());
    }

    @ModifyVariable(method = "setAbsorptionAmount", at = @At("HEAD"), argsOnly = true)
    private float capAbsorption(float amount) {
        float max = (float) getAttributeValue(PortAttributesExtension.maxAbsorption());
        return max > 0 ? Math.min(amount, max) : amount;
    }

    @ModifyReturnValue(method = "getScale", at = @At("RETURN"))
    private float applyScale(float original) {
        @Nullable AttributeMap attributes = getAttributes();
        if (attributes != null) { // maybe happens in super()
            return (float) (original * attributes.getValue(PortAttributesExtension.scale().value()));
        }
        return original;
    }

    @ModifyExpressionValue(method = "getJumpPower", at = @At(value = "CONSTANT", args = "floatValue=0.42"))
    private float applyJumpStrength(float original) {
        return (float) getAttributeValue(PortAttributesExtension.jumpStrength());
    }

    // endregion attributes

    // region onEffectStarted

    @ModifyReturnValue(method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z", at = @At(value = "RETURN", ordinal = 1))
    private boolean start1(boolean original, @Local(argsOnly = true) MobEffectInstance effectInstance) {
        IPortMobEffectInstanceExtension.of(effectInstance).onEffectAdded(portlib$self());
        IPortMobEffectInstanceExtension.of(effectInstance).onEffectStarted(portlib$self());
        return original;
    }

    @ModifyReturnValue(method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z", at = @At(value = "RETURN", ordinal = 2))
    private boolean start2(boolean original, @Local(argsOnly = true) MobEffectInstance effectInstance) {
        IPortMobEffectInstanceExtension.of(effectInstance).onEffectStarted(portlib$self());
        return original;
    }

    @ModifyReturnValue(method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z", at = @At(value = "RETURN", ordinal = 3))
    private boolean start3(boolean original, @Local(argsOnly = true) MobEffectInstance effectInstance) {
        IPortMobEffectInstanceExtension.of(effectInstance).onEffectStarted(portlib$self());
        return original;
    }

    // endregion
}
