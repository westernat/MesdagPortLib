package org.mesdag.portlib.diff.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import org.mesdag.portlib.diff.PortMouseHandler;
import org.mesdag.portlib.event.PortEventHandler;
import org.mesdag.portlib.event.client.PortCalculatePlayerTurnEvent;
import org.mesdag.portlib.wrapper.common.util.PortTriState;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public abstract class MouseHandlerMixin {
    @ModifyExpressionValue(method = "turnPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/OptionInstance;get()Ljava/lang/Object;", ordinal = 0))
    private <T> T getTurnPlayerValues(T original, @Share("event") LocalRef<PortCalculatePlayerTurnEvent> ev) {
        var event = new PortCalculatePlayerTurnEvent((Double) original);
        PortEventHandler.postEvent(event);
        ev.set(event);
        return (T) Double.valueOf(event.getMouseSensitivity());
    }

    @ModifyExpressionValue(method = "turnPlayer", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Options;smoothCamera:Z", opcode = Opcodes.GETFIELD))
    private boolean getCinematicCameraEnabled(boolean original, @Share("event") LocalRef<PortCalculatePlayerTurnEvent> ev) {
        PortTriState state = ev.get().getCinematicCameraEnabled();
        if (state.isDefault()) {
            return original;
        }
        return state.isTrue();
    }

    @Inject(method = "onScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;getOverlay()Lnet/minecraft/client/gui/screens/Overlay;"))
    private void catchScrollDelta(
            CallbackInfo ci,
            @Local(argsOnly = true, ordinal = 0) double xOffset,
            @Local(argsOnly = true, ordinal = 1) double yOffset,
            @Local(name = "d0") double scrollDelta
    ) {
        PortMouseHandler.xOffset = xOffset;
        PortMouseHandler.yOffset = yOffset;
        if (Minecraft.ON_OSX && yOffset == 0) {
            PortMouseHandler.scrollDeltaX = scrollDelta;
        } else {
            PortMouseHandler.scrollDeltaY = scrollDelta;
        }
    }
}
