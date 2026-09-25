package org.mesdag.portlib.diff.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.sounds.Music;
import org.mesdag.portlib.event.PortEventHandler;
import org.mesdag.portlib.event.client.PortSelectMusicEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(MusicManager.class)
public abstract class MusicManagerMixin {
    @Shadow
    @Nullable
    private SoundInstance currentMusic;

    @Shadow
    private int nextSongDelay;

    @Shadow
    public abstract void stopPlaying();

    /// 在 tick() 开头抛出 [PortSelectMusicEvent]，并把结果交给 [#portlib$applySelectedMusic]。
    ///
    /// 与 NeoForge 的 ClientHooks.selectMusic + MusicManager 补丁保持一致：监听者把音乐置为 null
    /// 表示“不要任何音乐”，此时必须停止当前音乐、清零延迟并跳过本刻剩余逻辑，
    /// 否则原版会在 `music.getEvent()` 上空指针崩溃。
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void portlib$selectMusic(CallbackInfo ci, @Share("selectedMusic") LocalRef<Music> selectedMusic) {
        Music original = Minecraft.getInstance().getSituationalMusic();
        PortSelectMusicEvent event = new PortSelectMusicEvent(original, currentMusic);
        PortEventHandler.postEvent(event);
        Music selected = event.getMusic();
        selectedMusic.set(selected);
        if (selected == null) {
            if (currentMusic != null) {
                stopPlaying();
            }
            nextSongDelay = 0;
            ci.cancel();
        }
    }

    @ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;getSituationalMusic()Lnet/minecraft/sounds/Music;"))
    private Music portlib$applySelectedMusic(Music original, @Share("selectedMusic") LocalRef<Music> selectedMusic) {
        Music selected = selectedMusic.get();
        return selected == null ? original : selected;
    }
}
