package org.mesdag.portlib.wrapper.world.entity;

import net.minecraft.world.entity.LivingEntity;
import org.mesdag.portlib.wrapper.common.PortEffectCure;

public class PortLivingEntity {
    public static boolean removeEffectsCuredBy(LivingEntity self, PortEffectCure cure) {
        return self.removeEffectsCuredBy(cure.unwrap());
    }
}
