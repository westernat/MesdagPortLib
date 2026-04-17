package PortLib.extensions.net.minecraft.world.effect.MobEffectInstance;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.minecraft.world.effect.MobEffectInstance;
import org.mesdag.portlib.util.PortSets;
import org.mesdag.portlib.wrapper.common.PortEffectCure;

import java.util.Set;

@Extension
public class PortMobEffectInstanceExtension {
    public static Set<PortEffectCure> getPortCures(@This MobEffectInstance thiz) {
        return PortSets.mutableTransform(thiz.getCures(), PortEffectCure::wrap, PortEffectCure::unwrap);
    }
}
