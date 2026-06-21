package org.mesdag.portlib.diff;

import net.minecraft.world.effect.MobEffectInstance;
import org.mesdag.portlib.wrapper.PortSelfGetter;
import org.mesdag.portlib.wrapper.common.PortEffectCure;
import org.mesdag.portlib.wrapper.common.extensions.IPortMobEffectInstanceExtension;

import java.util.Set;

public interface IPortMobEffectInstance extends PortSelfGetter<MobEffectInstance>, IPortMobEffectInstanceExtension {
    Set<PortEffectCure> portlib$getCures();

    static IPortMobEffectInstance of(MobEffectInstance instance) {
        return (IPortMobEffectInstance) instance;
    }
}
