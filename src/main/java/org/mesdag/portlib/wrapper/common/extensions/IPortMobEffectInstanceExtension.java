package org.mesdag.portlib.wrapper.common.extensions;

import PortLib.extensions.net.minecraft.world.effect.MobEffectInstance.PortMobEffectInstanceExtension;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import org.mesdag.portlib.wrapper.common.PortEffectCure;

import java.util.Set;
import java.util.function.Supplier;

@SuppressWarnings("all")
public interface IPortMobEffectInstanceExtension {

    private MobEffectInstance self() {
        return (MobEffectInstance) this;
    }

    default Set<PortEffectCure> getPortCures() {
        return PortMobEffectInstanceExtension.getPortCures(self());
    }

    default ParticleOptions getParticleOptions() {
        return PortMobEffectInstanceExtension.getParticleOptions(self());
    }

    default boolean is(Supplier<MobEffect> effectSupplier) {
        return PortMobEffectInstanceExtension.is(self(), effectSupplier);
    }

    default Set<PortEffectCure> getCures() {
        return PortMobEffectInstanceExtension.getCures(self());
    }

    static IPortMobEffectInstanceExtension of(MobEffectInstance instance) {
        return (IPortMobEffectInstanceExtension) instance;
    }
}
