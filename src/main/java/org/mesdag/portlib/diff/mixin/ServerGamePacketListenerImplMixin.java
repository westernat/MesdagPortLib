package org.mesdag.portlib.diff.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.network.protocol.game.ServerboundClientInformationPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.mesdag.portlib.event.PortEventHandler;
import org.mesdag.portlib.event.entity.player.PortClientInformationUpdatedEvent;
import org.mesdag.portlib.wrapper.server.level.PortClientInformation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerImplMixin {
    @WrapOperation(method = "handleClientInformation", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;updateOptions(Lnet/minecraft/network/protocol/game/ServerboundClientInformationPacket;)V"))
    private void post(ServerPlayer instance, ServerboundClientInformationPacket packet, Operation<Void> original) {
        PortClientInformation oldInfo = PortClientInformation.wrap(instance);
        original.call(instance, packet);
        PortEventHandler.postEvent(new PortClientInformationUpdatedEvent(instance, oldInfo, PortClientInformation.wrap(packet)));
    }
}
