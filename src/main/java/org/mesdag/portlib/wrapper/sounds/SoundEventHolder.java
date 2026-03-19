package org.mesdag.portlib.wrapper.sounds;

import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.mesdag.portlib.wrapper.core.PortHolder;

public class SoundEventHolder implements PortHolder<SoundEvent> {
    private final Holder<SoundEvent> delegate;

    private SoundEventHolder(SoundEvent value) {
        this.delegate = PortHolder.getDelegate(ForgeRegistries.SOUND_EVENTS, value);
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
