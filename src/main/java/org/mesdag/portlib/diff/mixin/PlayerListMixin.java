package org.mesdag.portlib.diff.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.phys.Vec3;
import org.mesdag.portlib.diff.attachment.PortAttachmentSync;
import org.mesdag.portlib.event.PortEventHandler;
import org.mesdag.portlib.event.entity.player.PortPlayerRespawnPositionEvent;
import org.mesdag.portlib.wrapper.world.level.portal.PortDimensionTransition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(PlayerList.class)
public abstract class PlayerListMixin {
    @Inject(method = "respawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;setHealth(F)V", shift = At.Shift.AFTER))
    private void syncInitialPlayerAttachments(CallbackInfoReturnable<ServerPlayer> cir, @Local(argsOnly = true) ServerPlayer player) {
        PortAttachmentSync.syncInitialPlayerAttachments(player);
    }

    @Inject(method = "placeNewPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;initInventoryMenu()V", shift = At.Shift.AFTER))
    private void syncInitialPlayerAttachments(CallbackInfo ci, @Local(argsOnly = true) ServerPlayer player) {
        PortAttachmentSync.syncInitialPlayerAttachments(player);
    }

    @Inject(method = "sendLevelInfo", at = @At("TAIL"))
    private void syncInitialPlayerAttachments(ServerPlayer player, ServerLevel level, CallbackInfo ci) {
        PortAttachmentSync.syncInitialLevelAttachments(level, player);
    }

    // region PlayerRespawnPositionEvent

    @Inject(method = "respawn", at = @At(value = "NEW", target = "(Lnet/minecraft/server/MinecraftServer;Lnet/minecraft/server/level/ServerLevel;Lcom/mojang/authlib/GameProfile;)Lnet/minecraft/server/level/ServerPlayer;"))
    private void firePlayerRespawnPositionEvent(
            ServerPlayer player,
            boolean keepEverything,
            CallbackInfoReturnable<ServerPlayer> cir,
            @Local(name = "blockpos") BlockPos blockpos,
            @Local(name = "optional") Optional<Vec3> optional,
            @Local(name = "serverlevel1") ServerLevel serverlevel1,
            @Share("event") LocalBooleanRef copyOriginalSpawnPosition,
            @Share("dimensiontransition") LocalRef<PortDimensionTransition> dimensiontransition
    ) {
        PortDimensionTransition transition = new PortDimensionTransition(serverlevel1, optional.isEmpty() && blockpos != null);
        PortPlayerRespawnPositionEvent event = new PortPlayerRespawnPositionEvent(player, transition, keepEverything);
        PortEventHandler.postEvent(event);
        copyOriginalSpawnPosition.set(event.copyOriginalSpawnPosition());
        dimensiontransition.set(event.getDimensionTransition());
    }

    @Inject(method = "respawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;setMainArm(Lnet/minecraft/world/entity/HumanoidArm;)V", shift = At.Shift.AFTER))
    private void copyOriginalSpawnPosition(
            CallbackInfoReturnable<ServerPlayer> cir,
            @Local(argsOnly = true) ServerPlayer player,
            @Local(name = "serverplayer") ServerPlayer serverplayer,
            @Share("event") LocalBooleanRef copyOriginalSpawnPosition
    ) {
        if (copyOriginalSpawnPosition.get()) {
            serverplayer.setRespawnPosition(
                    player.getRespawnDimension(),
                    player.getRespawnPosition(),
                    player.getRespawnAngle(),
                    player.isRespawnForced(),
                    false
            );
        }
    }

    @ModifyVariable(method = "respawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"), name = "blockpos")
    private BlockPos blockpos(BlockPos original, @Share("dimensiontransition") LocalRef<PortDimensionTransition> dimensiontransition) {
        return BlockPos.containing(dimensiontransition.get().pos().apply(Vec3.atBottomCenterOf(original)));
    }

    @ModifyVariable(method = "respawn", at = @At("STORE"), name = "vec3")
    private Vec3 vec3(Vec3 original, @Share("dimensiontransition") LocalRef<PortDimensionTransition> dimensiontransition) {
        return dimensiontransition.get().pos().apply(original);
    }

    @ModifyArg(method = "respawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;moveTo(DDDFF)V"), index = 3)
    private float yRot(float original, @Share("dimensiontransition") LocalRef<PortDimensionTransition> dimensiontransition) {
        return dimensiontransition.get().yRot().apply(original);
    }

    @ModifyArg(method = "respawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;moveTo(DDDFF)V"), index = 4)
    private float xRot(float original, @Share("dimensiontransition") LocalRef<PortDimensionTransition> dimensiontransition) {
        return dimensiontransition.get().xRot().apply(original);
    }

    // endregion PlayerRespawnPositionEvent
}
