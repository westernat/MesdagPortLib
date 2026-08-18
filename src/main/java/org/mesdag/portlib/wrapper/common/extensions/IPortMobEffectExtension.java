package org.mesdag.portlib.wrapper.common.extensions;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;

public interface IPortMobEffectExtension {
    default void onEffectStarted(LivingEntity living, int amplifier) {}

    default void onEffectAdded(LivingEntity living, int amplifier) {}

    static IPortMobEffectExtension of(MobEffect effect) {
        return (IPortMobEffectExtension) effect;
    }
}
