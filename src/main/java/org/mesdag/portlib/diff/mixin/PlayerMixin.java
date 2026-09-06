package org.mesdag.portlib.diff.mixin;

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
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.mesdag.portlib.diff.IPortFoodProperties;
import org.mesdag.portlib.diff.IPortLivingEntity;
import org.mesdag.portlib.diff.IPortPlayer;
import org.mesdag.portlib.event.PortEventHandler;
import org.mesdag.portlib.event.entity.living.PortLivingDamageEvent;
import org.mesdag.portlib.event.entity.player.PortCanContinueSleepingEvent;
import org.mesdag.portlib.event.entity.player.PortSweepAttackEvent;
import org.mesdag.portlib.wrapper.common.damagesource.PortDamageContainer;
import org.mesdag.portlib.wrapper.common.extensions.IPortAttributesExtension;
import org.mesdag.portlib.wrapper.common.extensions.IPortLivingEntityExtension;
import org.mesdag.portlib.wrapper.common.extensions.IPortPlayerExtension;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(Player.class)
public abstract class PlayerMixin implements IPortPlayer {
    @Shadow
    public abstract Inventory getInventory();

    @Shadow
    public abstract @Nullable ItemEntity drop(ItemStack itemStack, boolean includeThrowerName);

    @Shadow
    public abstract void setAbsorptionAmount(float amount);

    @Shadow
    public abstract float getAbsorptionAmount();

    @Shadow
    @Final
    protected static EntityDataAccessor<Byte> DATA_PLAYER_MODE_CUSTOMISATION;

    @Override
    public int portlib$getModelCustomisation() {
        return portlib$self().getEntityData().get(DATA_PLAYER_MODE_CUSTOMISATION);
    }

    @Inject(method = "eat", at = @At("HEAD"))
    private void captureFoodProperties(
            Level level,
            ItemStack food,
            CallbackInfoReturnable<ItemStack> cir,
            @Share("foodProperties") LocalRef<FoodProperties> foodProperties
    ) {
        // 原版会在 eat 返回前把最后一份食物减为空栈，必须在消费前保留本次食物属性。
        foodProperties.set(food.getFoodProperties(portlib$self()));
    }

    @ModifyReturnValue(method = "eat", at = @At("RETURN"))
    private ItemStack usingConvertsTo(
            ItemStack original,
            @Share("foodProperties") LocalRef<FoodProperties> foodPropertiesRef
    ) {
        FoodProperties foodProperties = foodPropertiesRef.get();
        if (foodProperties != null) {
            ItemStack stack = IPortFoodProperties.of(foodProperties).portlib$getUsingConvertsTo();
            if (stack != null && !IPortPlayerExtension.of(portlib$self()).hasInfiniteMaterials()) {
                if (original.isEmpty()) {
                    return stack.copy();
                }

                if (!portlib$self().level().isClientSide) {
                    ItemStack container = stack.copy();
                    if (!getInventory().add(container)) {
                        drop(container, false);
                    }
                }
            }
        }
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
    @WrapOperation(method = "actuallyHurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getDamageAfterArmorAbsorb(Lnet/minecraft/world/damagesource/DamageSource;F)F"))
    private float setArmorReduction(Player instance, DamageSource damageSource, float damageAmount, Operation<Float> original) {
        PortDamageContainer container = IPortLivingEntity.of(portlib$self()).portlib$getDamageContainers().peek();
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
    @ModifyExpressionValue(method = "actuallyHurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getDamageAfterMagicAbsorb(Lnet/minecraft/world/damagesource/DamageSource;F)F"))
    private float onLivingDamagePre(float original, @Share("absorbed") LocalFloatRef absorbed) {
        PortDamageContainer container = IPortLivingEntity.of(portlib$self()).portlib$getDamageContainers().peek();
        float damage = PortLivingDamageEvent.Pre.onLivingDamagePre(portlib$self(), container);
        container.setReduction(PortDamageContainer.PortReduction.ABSORPTION, Math.min(getAbsorptionAmount(), damage));
        absorbed.set(Math.min(damage, container.getReduction(PortDamageContainer.PortReduction.ABSORPTION)));
        setAbsorptionAmount(Math.max(0, getAbsorptionAmount() - absorbed.get()));
        return damage; // 其实已经被ignored
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
        return IPortLivingEntity.of(portlib$self()).portlib$getDamageContainers().peek().getNewDamage();
    }

    @WrapWithCondition(method = "actuallyHurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;setAbsorptionAmount(F)V"))
    private boolean deny(Player instance, float amount) {
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

    @Inject(method = "actuallyHurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;gameEvent(Lnet/minecraft/world/level/gameevent/GameEvent;)V", shift = At.Shift.AFTER))
    private void onDamageTaken(CallbackInfo ci) {
        IPortLivingEntityExtension.of(portlib$self()).onDamageTaken(IPortLivingEntity.of(portlib$self()).portlib$getDamageContainers().peek());
    }

    // endregion actuallyHurt

    @WrapWithCondition(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;stopSleepInBed(ZZ)V"))
    private boolean canEntityContinueSleeping(Player instance, boolean wakeImmediately, boolean updateLevelForSleepingPlayers) {
        return PortCanContinueSleepingEvent.canEntityContinueSleeping(portlib$self(), portlib$self().level().isDay() ? Player.BedSleepingProblem.NOT_POSSIBLE_NOW : null);
    }

    @ModifyVariable(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getFireAspect(Lnet/minecraft/world/entity/LivingEntity;)I"), name = "flag3")
    private boolean fireSweepAttack(boolean flag3, @Local(argsOnly = true) Entity target) {
        return PortEventHandler.postEventWithReturn(new PortSweepAttackEvent(portlib$self(), target, flag3)).isSweeping();
    }

    // region attributes

    @ModifyVariable(method = "getDigSpeed", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getBlockEfficiency(Lnet/minecraft/world/entity/LivingEntity;)I"), name = "f")
    private float miningEfficiency(float f) {
        return f + (float) portlib$self().getAttributeValue(IPortAttributesExtension.MINING_EFFICIENCY);
    }

    @ModifyVariable(method = "getDigSpeed", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isEyeInFluid(Lnet/minecraft/tags/TagKey;)Z"), name = "f")
    private float blockBreakSpeed(float f) {
        return f * (float) portlib$self().getAttributeValue(IPortAttributesExtension.BLOCK_BREAK_SPEED);
    }

    @ModifyExpressionValue(method = "travel", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/player/Abilities;flying:Z", opcode = Opcodes.GETFIELD))
    private boolean allowCreativeFlight(boolean original) {
        return original || portlib$self().getAttributeValue(IPortAttributesExtension.CREATIVE_FLIGHT) > 0;
    }

    @ModifyReturnValue(method = "getFlyingSpeed", at = @At("RETURN"))
    private float applyFlyingSpeed(float original) {
        return (float) (original * portlib$self().getAttributeValue(Attributes.FLYING_SPEED));
    }

    @ModifyExpressionValue(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getSweepingDamageRatio(Lnet/minecraft/world/entity/LivingEntity;)F"))
    private float applySweepingDamageRatio(float original) {
        return (float) (original + portlib$self().getAttributeValue(IPortAttributesExtension.SWEEPING_DAMAGE_RATIO));
    }

    // endregion attributes
}
