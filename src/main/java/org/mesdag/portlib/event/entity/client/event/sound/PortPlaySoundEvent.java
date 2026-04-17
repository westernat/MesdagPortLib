

package org.mesdag.portlib.event.entity.client.event.sound;

import net.minecraft.client.resources.sounds.SoundInstance;
import net.neoforged.neoforge.client.event.sound.PlaySoundEvent;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortPlaySoundEvent extends PortEvent {

    private final PlaySoundEvent e;

    @Diff
    public PortPlaySoundEvent(PlaySoundEvent e) {
        super(e);
        this.e = e;
    }

    public String getName() {
        return e.getName();
    }

    public SoundInstance getOriginalSound() {
        return e.getOriginalSound();
    }

    @Nullable
    public SoundInstance getSound() {
        return e.getSound();
    }

    public void setSound(@Nullable SoundInstance newSound) {
        e.setSound(newSound);
    }

    static {PortEventHooks.register(PlaySoundEvent.class, PortPlaySoundEvent.class, PortPlaySoundEvent::new);
    }
}