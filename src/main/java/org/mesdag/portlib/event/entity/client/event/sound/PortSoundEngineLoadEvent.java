
package org.mesdag.portlib.event.entity.client.event.sound;

import net.minecraft.client.sounds.SoundEngine;
import net.neoforged.neoforge.client.event.sound.SoundEngineLoadEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortSoundEngineLoadEvent extends PortEvent {
    private final SoundEngineLoadEvent e;

    @Diff
    public PortSoundEngineLoadEvent(SoundEngineLoadEvent e) {
        super(e);
        this.e = e;
    }

    public SoundEngine getEngine() {
        return e.getEngine();
    }

    static {
        PortEventHooks.register(SoundEngineLoadEvent.class, PortSoundEngineLoadEvent.class, PortSoundEngineLoadEvent::new);
    }
}