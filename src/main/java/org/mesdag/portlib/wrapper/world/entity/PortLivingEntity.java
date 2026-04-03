package org.mesdag.portlib.wrapper.world.entity;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.mesdag.portlib.diff.IPortMobEffectInstance;
import org.mesdag.portlib.diff.mixin.LivingEntityAccessor;
import org.mesdag.portlib.event.entity.living.PortMobEffectEvent;
import org.mesdag.portlib.wrapper.common.PortEffectCure;

import java.util.Iterator;

public class PortLivingEntity {
    public static boolean removeEffectsCuredBy(LivingEntity self, PortEffectCure cure) {
        if (self.level().isClientSide) {
            return false;
        }
        boolean ret = false;
        Iterator<MobEffectInstance> itr = self.getActiveEffectsMap().values().iterator();
        while (itr.hasNext()) {
            MobEffectInstance effect = itr.next();
            if (IPortMobEffectInstance.of(effect).portlib$getCures().contains(cure) && !PortMobEffectEvent.PortRemove.onEffectRemoved(self, effect, cure)) {
                ((LivingEntityAccessor) self).callOnEffectRemoved(effect);
                itr.remove();
                ret = true;
                self.effectsDirty = true;
            }
        }
        return ret;
    }
}
