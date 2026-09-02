package org.mesdag.portlib.wrapper.common.extensions;

import net.minecraft.core.Holder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.diff.IPortMobEffectInstance;
import org.mesdag.portlib.diff.mixin.LivingEntityAccessor;
import org.mesdag.portlib.event.entity.living.PortMobEffectEvent;
import org.mesdag.portlib.wrapper.common.PortEffectCure;
import org.mesdag.portlib.wrapper.common.damagesource.PortDamageContainer;

import java.util.Iterator;

public interface IPortLivingEntityExtension extends IPortEntityExtension {
    private LivingEntity self() {
        return (LivingEntity) this;
    }

    /// @return if false, it will skip original onDamageTaken invoke (Neoforge only)
    default boolean onDamageTaken(PortDamageContainer container) {
        return true;
    }

    default @Nullable AttributeInstance getAttribute(Holder<Attribute> attribute) {
        return self().getAttribute(attribute.value());
    }

    default boolean removeEffectsCuredBy(PortEffectCure cure) {
        if (self().level().isClientSide) {
            return false;
        }
        boolean ret = false;
        Iterator<MobEffectInstance> itr = self().getActiveEffectsMap().values().iterator();
        while (itr.hasNext()) {
            MobEffectInstance effect = itr.next();
            if (IPortMobEffectInstance.of(effect).portlib$getCures().contains(cure) && !PortMobEffectEvent.Remove.onEffectRemoved(self(), effect, cure)) {
                ((LivingEntityAccessor) self()).callOnEffectRemoved(effect);
                itr.remove();
                ret = true;
                self().effectsDirty = true;
            }
        }
        return ret;
    }

    @Override
    default @NotNull ItemStack getWeaponItem() {
        return self().getMainHandItem();
    }

    default boolean hasInfiniteMaterials() {
        return false;
    }

    @Diff
    default boolean hasEffect(RegistryObject<? extends MobEffect> effect) {
        return self().hasEffect(effect.get());
    }

    @Diff
    default boolean removeEffect(RegistryObject<? extends MobEffect> effect) {
        return self().removeEffect(effect.get());
    }

    static EquipmentSlot getSlotForHand(InteractionHand hand) {
        return hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
    }

    static IPortLivingEntityExtension of(LivingEntity living) {
        return (IPortLivingEntityExtension) living;
    }
}
