package org.mesdag.portlib.event.entity.living;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.common.extensions.IPortMobEffectInstanceExtension;
import org.mesdag.portlib.wrapper.world.effect.PortMobEffect;

import java.util.Objects;

public class PortEffectParticleModificationEvent extends LivingEvent {
    private final MobEffectInstance effect;
    private final ParticleOptions originalOptions;
    private ParticleOptions options;
    private boolean isVisible;

    @Diff
    public PortEffectParticleModificationEvent(LivingEntity entity, MobEffectInstance effect) {
        super(entity);
        this.effect = effect;
        this.isVisible = effect.isVisible();
        this.originalOptions = IPortMobEffectInstanceExtension.of(effect).getParticleOptions();
        this.options = originalOptions;
    }

    public MobEffectInstance getEffect() {
        return effect;
    }

    public ParticleOptions getOriginalParticleOptions() {
        return originalOptions;
    }

    public ParticleOptions getParticleOptions() {
        return options;
    }

    public void setParticleOptions(@Nullable ParticleOptions options) {
        this.options = Objects.requireNonNullElse(options, originalOptions);
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        this.isVisible = visible;
    }

    @Diff
    public boolean isCustomParticle() {
        return isVisible() && (effect.getEffect() instanceof PortMobEffect || !Objects.equals(originalOptions, options));
    }
}
