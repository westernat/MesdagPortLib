package org.mesdag.portlib.event.entity.living;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.neoforge.event.entity.living.EffectParticleModificationEvent;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;

// todo wait for 1.20.1
public class PortEffectParticleModificationEvent extends PortLivingEvent<EffectParticleModificationEvent> {
    @Diff
    public PortEffectParticleModificationEvent(EffectParticleModificationEvent e) {
        super(e);
    }

    public MobEffectInstance getEffect() {
        return e.getEffect();
    }

    public ParticleOptions getOriginalParticleOptions() {
        return e.getOriginalParticleOptions();
    }

    public ParticleOptions getParticleOptions() {
        return e.getParticleOptions();
    }

    public void setParticleOptions(@Nullable ParticleOptions options) {
        e.setParticleOptions(options);
    }

    public boolean isVisible() {
        return e.isVisible();
    }

    public void setVisible(boolean visible) {
        e.setVisible(visible);
    }

    static {
        PortEventHooks.register(EffectParticleModificationEvent.class, PortEffectParticleModificationEvent.class, PortEffectParticleModificationEvent::new);
    }
}
