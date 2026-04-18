package org.mesdag.portlib.event.client.sound;

import net.minecraft.client.resources.sounds.SoundInstance;
import net.neoforged.neoforge.client.event.sound.PlaySoundEvent;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;

public class PortPlaySoundEvent extends PortSoundEvent<PlaySoundEvent> {
    @Diff
    public PortPlaySoundEvent(PlaySoundEvent e) {
        super(e);
    }

    public String getName() {
        return e.getName();
    }

    public SoundInstance getOriginalSound() {
        return e.getOriginalSound();
    }

    public @Nullable SoundInstance getSound() {
        return e.getSound();
    }

    public void setSound(@Nullable SoundInstance newSound) {
        e.setSound(newSound);
    }

    static {
        PortEventHooks.register();
    }
}
