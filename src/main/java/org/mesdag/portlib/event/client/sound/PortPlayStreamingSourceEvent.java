package org.mesdag.portlib.event.client.sound;

import com.mojang.blaze3d.audio.Channel;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraftforge.client.event.sound.PlayStreamingSourceEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;

public class PortPlayStreamingSourceEvent extends PortSoundEvent<PlayStreamingSourceEvent> {
    @Diff
    public PortPlayStreamingSourceEvent(PlayStreamingSourceEvent e) {
        super(e);
    }

    public SoundInstance getSound() {
        return e.getSound();
    }

    public Channel getChannel() {
        return e.getChannel();
    }

    static {
        PortEventHooks.register();
    }
}
