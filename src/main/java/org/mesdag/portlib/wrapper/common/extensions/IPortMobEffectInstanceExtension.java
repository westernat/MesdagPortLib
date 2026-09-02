package org.mesdag.portlib.wrapper.common.extensions;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.mesdag.portlib.diff.IPortMobEffectInstance;
import org.mesdag.portlib.wrapper.common.PortEffectCure;
import org.mesdag.portlib.wrapper.world.effect.PortMobEffect;

import java.util.Set;
import java.util.function.Supplier;

@SuppressWarnings("all")
public interface IPortMobEffectInstanceExtension {
    private MobEffectInstance self() {
        return (MobEffectInstance) this;
    }

    default ParticleOptions getParticleOptions() {
        if (self().getEffect() instanceof PortMobEffect port) {
            return port.createParticleOptions(self());
        }
        return self().isAmbient() ? ParticleTypes.AMBIENT_ENTITY_EFFECT : ParticleTypes.ENTITY_EFFECT;
    }

    default boolean is(Supplier<MobEffect> effectSupplier) {
        return self().getEffect() == effectSupplier.get();
    }

    default Set<PortEffectCure> getCures() {
        return IPortMobEffectInstance.of(self()).portlib$getCures();
    }

    default void onEffectStarted(LivingEntity living) {
        IPortMobEffectExtension.of(self().getEffect()).onEffectStarted(living, self().getAmplifier());
    }

    default void onEffectAdded(LivingEntity living) {
        IPortMobEffectExtension.of(self().getEffect()).onEffectAdded(living, self().getAmplifier());
    }

    static IPortMobEffectInstanceExtension of(MobEffectInstance instance) {
        return (IPortMobEffectInstanceExtension) instance;
    }
}
