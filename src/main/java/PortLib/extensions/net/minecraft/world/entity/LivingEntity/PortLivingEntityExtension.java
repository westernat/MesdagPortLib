package PortLib.extensions.net.minecraft.world.entity.LivingEntity;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
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
            if (IPortMobEffectInstance.of(effect).portlib$getCures().contains(cure) && !PortMobEffectEvent.PortRemove.onEffectRemoved(thiz, effect, cure)) {
                ((LivingEntityAccessor) thiz).callOnEffectRemoved(effect);
                itr.remove();
                ret = true;
                thiz.effectsDirty = true;
            }
        }
        return ret;
    }
}
