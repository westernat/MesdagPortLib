package org.mesdag.portlib.diff.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.sounds.Music;
import org.mesdag.portlib.event.PortEventHandler;
import org.mesdag.portlib.event.client.PortSelectMusicEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import javax.annotation.Nullable;

@Mixin(MusicManager.class)
public abstract class MusicManagerMixin {
    @Shadow
    @Nullable
    private SoundInstance currentMusic;

    @ModifyExpressionValue(method = "tick",at= @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;getSituationalMusic()Lnet/minecraft/sounds/Music;"))
    private Music selectMusic(Music original) {
        var event = new PortSelectMusicEvent(original, currentMusic);
        PortEventHandler.postEvent(event);
        return event.getMusic();
    }
}
