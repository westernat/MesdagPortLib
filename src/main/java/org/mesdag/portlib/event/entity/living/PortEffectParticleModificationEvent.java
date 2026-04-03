package org.mesdag.portlib.event.entity.living;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent;

// todo
public class PortEffectParticleModificationEvent extends LivingEvent {
    public PortEffectParticleModificationEvent(LivingEntity entity) {
        super(entity);
    }
//    private final MobEffectInstance effect;
//    private final ParticleOptions originalOptions;
//    private ParticleOptions options;
//    private boolean isVisible;
//
//    @Diff
//    public PortEffectParticleModificationEvent(LivingEntity entity, MobEffectInstance effect) {
//        super(entity);
//        this.effect = effect;
//        this.isVisible = effect.isVisible();
//        this.originalOptions = effect.getParticleOptions();
//        this.options = this.originalOptions;
//    }
//
//    public MobEffectInstance getEffect() {
//        return effect;
//    }
//
//    public ParticleOptions getOriginalParticleOptions() {
//        return originalOptions;
//    }
//
//    public ParticleOptions getParticleOptions() {
//        return options;
//    }
//
//    public void setParticleOptions(@Nullable ParticleOptions options) {
//        this.options = Objects.requireNonNullElse(options, originalOptions);
//    }
//
//    public boolean isVisible() {
//        return isVisible;
//    }
//
//    public void setVisible(boolean visible) {
//        isVisible = visible;
//    }
}
