package org.mesdag.portlib.event.client;

import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.Music;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;

@Cancelable
public class PortSelectMusicEvent extends Event {
    private @Nullable Music music;
    private final Music originalMusic;
    private final @Nullable SoundInstance playingMusic;

    @Diff
    public PortSelectMusicEvent(Music music, @Nullable SoundInstance playingMusic) {
        this.music = music;
        this.originalMusic = music;
        this.playingMusic = playingMusic;
    }

    public Music getOriginalMusic() {
        return originalMusic;
    }

    public @Nullable SoundInstance getPlayingMusic() {
        return playingMusic;
    }

    public @Nullable Music getMusic() {
        return music;
    }

    public void setMusic(@Nullable Music newMusic) {
        this.music = newMusic;
    }

    public void overrideMusic(@Nullable Music newMusic) {
        this.music = newMusic;
        this.setCanceled(true);
    }
}
