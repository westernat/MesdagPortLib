

package org.mesdag.portlib.event.entity.client.event.sound;

import com.mojang.blaze3d.audio.Channel;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import net.neoforged.neoforge.client.event.sound.PlayStreamingSourceEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortPlayStreamingSourceEvent extends PortEvent {
    private final PlayStreamingSourceEvent e;

    @Diff
    public PortPlayStreamingSourceEvent(PlayStreamingSourceEvent e) {
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

    static {
        PortEventHooks.register(PlayStreamingSourceEvent.class, PortPlayStreamingSourceEvent.class, PortPlayStreamingSourceEvent::new);
    }
}