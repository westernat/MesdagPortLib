package org.mesdag.portlib.diff.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import org.mesdag.portlib.event.PortEventHandler;
import org.mesdag.portlib.event.tick.PortEntityTickEvent;
import org.mesdag.portlib.wrapper.common.extensions.IPortClientLevelExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin implements IPortClientLevelExtension {
    @WrapOperation(method = "tickNonPassenger", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;tick()V"))
    private void fireEntityTick(Entity instance, Operation<Void> original) {
        if (!PortEventHandler.postEventWithReturn(new PortEntityTickEvent.Pre(instance)).isCanceled()) {
            original.call(instance);
            PortEventHandler.postEvent(new PortEntityTickEvent.Post(instance));
        }
    }
}
