package org.mesdag.portlib.diff.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import org.mesdag.portlib.event.PortEventHandler;
import org.mesdag.portlib.event.client.PortClientPauseChangeEvent;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Shadow
    private volatile boolean pause;

    @ModifyExpressionValue(method = "runTick", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;pause:Z", opcode = Opcodes.GETFIELD, ordinal = 2))
    private boolean onClientPauseChangePre(boolean original, @Local(name = "flag1") boolean flag1) {
        var event = new PortClientPauseChangeEvent.Pre(flag1);
        PortEventHandler.postEvent(event);
        if (event.isCanceled()) {
            return flag1; // make pause == flag1
        }
        return original;
    }

    @Inject(method = "runTick", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;pause:Z", opcode = Opcodes.PUTFIELD, shift = At.Shift.AFTER))
    private void onClientPauseChangePost(CallbackInfo ci) {
        PortEventHandler.postEvent(new PortClientPauseChangeEvent.Post(pause));
    }
}
