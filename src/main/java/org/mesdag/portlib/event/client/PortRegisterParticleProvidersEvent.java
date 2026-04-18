package org.mesdag.portlib.event.client;

import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortRegisterParticleProvidersEvent extends PortEvent<RegisterParticleProvidersEvent> {
    @Diff
    public PortRegisterParticleProvidersEvent(RegisterParticleProvidersEvent e) {
        super(e);
    }

    public <T extends ParticleOptions> void registerSpecial(ParticleType<T> type, ParticleProvider<T> provider) {
        e.registerSpecial(type, provider);
    }

    public <T extends ParticleOptions> void registerSprite(ParticleType<T> type, ParticleProvider.Sprite<T> sprite) {
        e.registerSprite(type, sprite);
    }

    public <T extends ParticleOptions> void registerSpriteSet(ParticleType<T> type, ParticleEngine.SpriteParticleRegistration<T> registration) {
        e.registerSpriteSet(type, registration);
    }

    static {
        PortEventHooks.register();
    }
}
