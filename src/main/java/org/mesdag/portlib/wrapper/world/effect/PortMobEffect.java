package org.mesdag.portlib.wrapper.world.effect;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class PortMobEffect extends MobEffect {
    public PortMobEffect(MobEffectCategory category, int color, ParticleOptions particle) {
        super(category, color, particle);
    }

    public PortMobEffect(MobEffectCategory category, int color) {
        super(category, color);
    }
}
