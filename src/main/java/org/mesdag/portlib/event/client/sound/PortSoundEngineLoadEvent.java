package org.mesdag.portlib.event.client.sound;

import net.neoforged.neoforge.client.event.sound.SoundEngineLoadEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;

public class PortSoundEngineLoadEvent extends PortSoundEvent<SoundEngineLoadEvent> {
    @Diff
    public PortSoundEngineLoadEvent(SoundEngineLoadEvent e) {
        super(e);
    }

    static {
        PortEventHooks.register();
    }
}
