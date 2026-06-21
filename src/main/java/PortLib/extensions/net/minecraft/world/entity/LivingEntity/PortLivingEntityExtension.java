package PortLib.extensions.net.minecraft.world.entity.LivingEntity;

import PortLib.extensions.net.minecraft.world.entity.Entity.PortEntityExtension;
import PortLib.extensions.net.minecraft.world.entity.player.Player.PortPlayerExtension;
import net.minecraft.core.Holder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.IPortMobEffectInstance;
import org.mesdag.portlib.diff.mixin.LivingEntityAccessor;
import org.mesdag.portlib.event.entity.living.PortMobEffectEvent;
import org.mesdag.portlib.wrapper.common.PortEffectCure;
import org.mesdag.portlib.wrapper.common.damagesource.PortDamageContainer;
import org.mesdag.portlib.wrapper.common.extensions.IPortLivingEntityExtension;

import java.util.Iterator;

public class PortLivingEntityExtension {
    /// @return if false, it will skip original onDamageTaken invoke (Neoforge only)
    public static boolean onDamageTaken(LivingEntity thiz, PortDamageContainer damageContainer) {
        return IPortLivingEntityExtension.of(thiz).onDamageTaken(damageContainer);
    }

    public static @Nullable AttributeInstance getAttribute(LivingEntity thiz, Holder<Attribute> attribute) {
        return thiz.getAttribute(attribute.value());
    }

    public static boolean removeEffectsCuredBy(LivingEntity thiz, PortEffectCure cure) {
        if (thiz.level().isClientSide) {
            return false;
        }
        boolean ret = false;
        Iterator<MobEffectInstance> itr = thiz.getActiveEffectsMap().values().iterator();
        while (itr.hasNext()) {
            MobEffectInstance effect = itr.next();
            if (IPortMobEffectInstance.of(effect).portlib$getCures().contains(cure) && !PortMobEffectEvent.Remove.onEffectRemoved(thiz, effect, cure)) {
                ((LivingEntityAccessor) thiz).callOnEffectRemoved(effect);
                itr.remove();
                ret = true;
                thiz.effectsDirty = true;
            }
        }
        return ret;
    }

    /// @see PortEntityExtension#getWeaponItem(Entity) super.getWeaponItem
    public static ItemStack getWeaponItem(LivingEntity thiz) {
        return thiz.getMainHandItem();
    }

    public static EquipmentSlot getSlotForHand(InteractionHand hand) {
        return hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
    }

    public static boolean hasInfiniteMaterials(@NotNull LivingEntity thiz) {
        if (thiz instanceof Player player) {
            return PortPlayerExtension.hasInfiniteMaterials(player);
        }
        return false;
    }
}
