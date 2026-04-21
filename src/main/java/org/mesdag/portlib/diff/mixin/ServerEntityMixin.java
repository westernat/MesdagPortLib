package org.mesdag.portlib.diff.mixin;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerPlayerConnection;
import net.minecraft.world.entity.Entity;
import org.mesdag.portlib.PortLib;
import org.mesdag.portlib.diff.IPortLivingEntity;
import org.mesdag.portlib.diff.PortSyncEffectParticlesS2C;
import org.mesdag.portlib.diff.attachment.PortAttachmentSync;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ServerEntity.class)
public abstract class ServerEntityMixin {
    @Shadow
    @Final
    private Entity entity;

    @Shadow
    @Final
    private ServerLevel level;

    @Shadow
    private int tickCount;

    @Shadow
    @Final
    private int updateInterval;

    @Inject(method = "sendPairingData", at = @At("TAIL"))
    private void syncInitialEntityAttachments(ServerPlayer player, Consumer<Packet<ClientGamePacketListener>> consumer, CallbackInfo ci) {
        PortAttachmentSync.syncInitialEntityAttachments(entity, player, consumer);

        if (entity instanceof IPortLivingEntity living && living.portlib$isDirty()) {
            living.portlib$setDirty(false);
            consumer.accept(PortLib.NETWORK_HANDLER.toVanillaClientbound(new PortSyncEffectParticlesS2C(entity.getId(), living.portlib$getEffectParticles())));
        }
    }

    @Inject(method = "sendChanges", at = @At("TAIL"))
    private void syncEffectParticles(CallbackInfo ci) {
        if (entity instanceof IPortLivingEntity living && living.portlib$isDirty() && tickCount % updateInterval == 0) {
            if (entity instanceof ServerPlayer player) {
                living.portlib$setDirty(false);
                player.connection.send(PortLib.NETWORK_HANDLER.toVanillaClientbound(new PortSyncEffectParticlesS2C(entity.getId(), living.portlib$getEffectParticles())));
            }
            ChunkMap.TrackedEntity trackedEntity = level.getChunkSource().chunkMap.entityMap.get(entity.getId());
            if (trackedEntity != null && (!trackedEntity.seenBy.isEmpty())) {
                living.portlib$setDirty(false);
                for (ServerPlayerConnection connection : trackedEntity.seenBy) {
                    connection.send(PortLib.NETWORK_HANDLER.toVanillaClientbound(new PortSyncEffectParticlesS2C(entity.getId(), living.portlib$getEffectParticles())));
                }
            }
        }
    }
}
