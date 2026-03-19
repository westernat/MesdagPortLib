package org.mesdag.portlib.wrapper.sounds;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import org.mesdag.portlib.wrapper.core.PortHolder;

public class SoundEventHolder implements PortHolder<SoundEvent> {
    private final Holder<SoundEvent> delegate;

    private SoundEventHolder(SoundEvent value) {
        this.delegate = BuiltInRegistries.SOUND_EVENT.wrapAsHolder(value);
    }

    private SoundEventHolder(Holder<SoundEvent> delegate) {
        this.delegate = delegate;
    }

    @Override
    public Holder<SoundEvent> delegate() {
        return delegate;
    }

    public static SoundEventHolder wrap(SoundEvent value) {
        return new SoundEventHolder(value);
    }

    public static SoundEventHolder wrap(Holder<SoundEvent> delegate) {
        return new SoundEventHolder(delegate);
    }
}
