package PortLib.extensions.net.minecraft.world.effect.MobEffect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import org.mesdag.portlib.wrapper.common.PortEffectCure;
import org.mesdag.portlib.wrapper.common.PortEffectCures;

import java.util.Set;

public class PortMobEffectExtension {
    public static void fillPortEffectCures(MobEffect thiz, Set<PortEffectCure> cures, MobEffectInstance effectInstance) {
        cures.addAll(PortEffectCures.DEFAULT_CURES);
        if (thiz == MobEffects.POISON) {
            cures.add(PortEffectCures.HONEY);
        }
    }
}
