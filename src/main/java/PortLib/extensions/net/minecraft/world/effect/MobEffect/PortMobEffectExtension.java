package PortLib.extensions.net.minecraft.world.effect.MobEffect;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import org.mesdag.portlib.wrapper.common.PortEffectCure;

import java.util.Set;

@Extension
public class PortMobEffectExtension {
    /// @return if false, it will skip original [fillEffectCures][net.neoforged.neoforge.common.extensions.IMobEffectExtension#fillEffectCures] invoke
    public static boolean fillPortEffectCures(@This MobEffect thiz, Set<PortEffectCure> cures, MobEffectInstance effectInstance) {
        return true;
    }
}
