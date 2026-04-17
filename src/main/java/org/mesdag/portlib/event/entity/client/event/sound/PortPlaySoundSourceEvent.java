
package org.mesdag.portlib.event.entity.client.event.sound;

import com.mojang.blaze3d.audio.Channel;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import net.neoforged.neoforge.client.event.sound.PlaySoundSourceEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortPlaySoundSourceEvent extends PortEvent {
    private final PlaySoundSourceEvent e;

    @Diff
    public PortPlaySoundSourceEvent(PlaySoundSourceEvent e) {
        super(e);
        this.e = e;
    }

    public SoundEngine getEngine() {
        return e.getEngine();
    }

    public SoundInstance getSound() {
        return e.getSound();
    }

    public Channel getChannel() {
        return e.getChannel();
    }

    static {PortEventHooks.register(PlaySoundSourceEvent.class, PortPlaySoundSourceEvent.class, PortPlaySoundSourceEvent::new);
    }
}