package org.mesdag.portlib.diff.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Camera;
import org.mesdag.portlib.event.PortEventHandler;
import org.mesdag.portlib.event.client.PortCalculateDetachedCameraDistanceEvent;
import org.mesdag.portlib.wrapper.PortSelfGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Camera.class)
public abstract class CameraMixin implements PortSelfGetter<Camera> {
    @ModifyArg(method = "setup", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;getMaxZoom(D)D"))
    private double getDetachedCameraDistance(double startingDistance, @Local(argsOnly = true, ordinal = 1) boolean thirdPersonReverse) {
        var event = new PortCalculateDetachedCameraDistanceEvent(portlib$self(), thirdPersonReverse, 1, (float) startingDistance);
        PortEventHandler.postEvent(event);
        return event.getDistance();
    }
}
