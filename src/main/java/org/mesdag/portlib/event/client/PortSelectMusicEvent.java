package org.mesdag.portlib.event.client;

import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.Music;
import net.neoforged.neoforge.client.event.SelectMusicEvent;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortSelectMusicEvent extends PortEvent<SelectMusicEvent> implements IPortCancellableEvent {
    @Diff
    public PortSelectMusicEvent(SelectMusicEvent e) {
        super(e);
    }

    public Music getOriginalMusic() {
        return e.getOriginalMusic();
    }

    public @Nullable SoundInstance getPlayingMusic() {
        return e.getPlayingMusic();
    }

    public @Nullable Music getMusic() {
        return e.getMusic();
    }

    public void setMusic(@Nullable Music newMusic) {
        e.setMusic(newMusic);
    }

    public void overrideMusic(@Nullable Music newMusic) {
        e.overrideMusic(newMusic);
    }

    static {
        PortEventHooks.register();
    }
}
