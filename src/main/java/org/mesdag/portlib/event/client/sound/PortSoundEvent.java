package org.mesdag.portlib.event.client.sound;

import com.mojang.blaze3d.audio.Channel;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraftforge.client.event.sound.SoundEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;

public abstract class PortSoundEvent<E extends SoundEvent> extends PortEvent<E> {
    @Diff
    public PortSoundEvent(E e) {
        super(e);
    }

    public SoundEngine getEngine() {
        return e.getEngine();
    }

    public static abstract class SoundSourceEvent<E extends SoundEvent.SoundSourceEvent> extends PortSoundEvent<E> {
        @Diff
        public SoundSourceEvent(E e) {
            super(e);
        }

        public SoundInstance getSound() {
            return e.getSound();
        }

        public Channel getChannel() {
            return e.getChannel();
        }

        public String getName() {
            return e.getName();
        }
    }
}
