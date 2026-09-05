package org.mesdag.portlib.diff.mixin;

import com.google.common.collect.Sets;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Cancellable;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.IPortMobEffectInstance;
import org.mesdag.portlib.wrapper.common.PortEffectCure;
import org.mesdag.portlib.wrapper.common.extensions.IPortMobEffectExtension;
import org.mesdag.portlib.wrapper.world.effect.PortMobEffect;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(MobEffectInstance.class)
public abstract class MobEffectInstanceMixin implements IPortMobEffectInstance {
    @Shadow
    @Final
    private MobEffect effect;

    @Shadow
    protected abstract boolean hasRemainingDuration();

    @Shadow
    private int amplifier;
    @Unique
    private final Set<PortEffectCure> portlib$cures = Sets.newIdentityHashSet();

    @Override
    public Set<PortEffectCure> portlib$getCures() {
        return portlib$cures;
    }

    @Inject(method = "<init>(Lnet/minecraft/world/effect/MobEffect;IIZZZLnet/minecraft/world/effect/MobEffectInstance;Ljava/util/Optional;)V", at = @At("TAIL"))
    private void fillEffectCures(CallbackInfo ci, @Local(argsOnly = true) MobEffect effect) {
        IPortMobEffectExtension.of(effect).fillPortEffectCures(portlib$cures, portlib$self());
    }

    @Inject(method = "setDetailsFrom", at = @At("TAIL"))
    private void setEffectCures(MobEffectInstance effectInstance, CallbackInfo ci) {
        portlib$cures.clear();
        portlib$cures.addAll(IPortMobEffectInstance.of(effectInstance).portlib$getCures());
    }

    @ModifyReturnValue(method = "load", at = @At("RETURN"))
    private static @Nullable MobEffectInstance loadCure(MobEffectInstance original, @Local(argsOnly = true) CompoundTag tag) {
        if (original != null && tag.contains("portlib:cures", Tag.TAG_STRING)) {
            Set<PortEffectCure> cures = IPortMobEffectInstance.of(original).portlib$getCures();
            for (Tag tag1 : tag.getList("portlib:cures", Tag.TAG_STRING)) {
                cures.add(PortEffectCure.get(tag1.getAsString()));
            }
        }
        return original;
    }

    @ModifyReturnValue(method = "save", at = @At("RETURN"))
    private CompoundTag saveCure(CompoundTag tag) {
        ListTag list = new ListTag();
        for (PortEffectCure cure : portlib$cures) {
            list.add(StringTag.valueOf(cure.name()));
        }
        tag.put("portlib:cures", list);
        return tag;
    }

    @WrapWithCondition(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/effect/MobEffectInstance;applyEffect(Lnet/minecraft/world/entity/LivingEntity;)V"))
    private boolean wrap(MobEffectInstance instance, LivingEntity entity, @Cancellable CallbackInfoReturnable<Boolean> cir) {
        if (effect instanceof PortMobEffect port) {
            if (hasRemainingDuration() && !port.applyEffectTick1211(entity, amplifier)) {
                cir.setReturnValue(false);
            }
            return false;
        }
        return true;
    }
}
