package org.mesdag.portlib.diff.mixin;

import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.chunk.LevelChunk;
import org.apache.commons.lang3.mutable.MutableObject;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.wrapper.common.extensions.IPortChunkMapExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkMap.class)
public abstract class ChunkMapMixin implements IPortChunkMapExtension {
    @Inject(method = "playerLoadedChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/game/DebugPackets;sendPoiPacketsForChunk(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/ChunkPos;)V", shift = At.Shift.AFTER))
    private void fireChunkSent(ServerPlayer player, MutableObject<ClientboundLevelChunkWithLightPacket> packetCache, LevelChunk chunk, CallbackInfo ci) {
        PortEventHooks.fireChunkSent(player, chunk, player.serverLevel());
    }
}
