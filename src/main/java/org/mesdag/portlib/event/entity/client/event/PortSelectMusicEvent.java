package org.mesdag.portlib.event.entity.client.event;

import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.Music;
import net.neoforged.neoforge.client.event.SelectMusicEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

import javax.annotation.Nullable;

public class PortSelectMusicEvent extends PortEvent {
    private final SelectMusicEvent e;

    @Diff
    public PortSelectMusicEvent(SelectMusicEvent e) {
        super(e);
        this.e = e;
    }

    public Music getOriginalMusic() {
        return e.getOriginalMusic();
    }

    @Nullable
    public SoundInstance getPlayingMusic() {
        return e.getPlayingMusic();
    }

    @Nullable
    public Music getMusic() {
        return e.getMusic();
    }

    public void setMusic(@Nullable Music newMusic) {
        e.setMusic(newMusic);
    }

    public void overrideMusic(@Nullable Music newMusic) {
        e.overrideMusic(newMusic);
    }

    public void setCanceled(boolean canceled) {
        e.setCanceled(canceled);
    }

    public boolean isCanceled() {
        return e.isCanceled();
    }

    static {
        PortEventHooks.register(SelectMusicEvent.class, PortSelectMusicEvent.class, PortSelectMusicEvent::new);
    }
}