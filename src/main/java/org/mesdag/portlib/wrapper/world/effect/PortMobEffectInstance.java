package org.mesdag.portlib.wrapper.world.effect;

import net.minecraft.world.effect.MobEffectInstance;
import org.mesdag.portlib.diff.IPortMobEffectInstance;
import org.mesdag.portlib.wrapper.common.PortEffectCure;

import java.util.Set;

public class PortMobEffectInstance {
    public static Set<PortEffectCure> getCures(MobEffectInstance self) {
        return IPortMobEffectInstance.of(self).portlib$getCures();
    }
}
