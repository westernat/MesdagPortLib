package org.mesdag.portlib.event.client.sound;

import net.minecraftforge.client.event.sound.PlaySoundSourceEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;

public class PortPlaySoundSourceEvent extends PortSoundEvent.SoundSourceEvent<PlaySoundSourceEvent> {
    @Diff
    public PortPlaySoundSourceEvent(PlaySoundSourceEvent e) {
        super(e);
    }

    static {
        PortEventHooks.register();
    }
}
