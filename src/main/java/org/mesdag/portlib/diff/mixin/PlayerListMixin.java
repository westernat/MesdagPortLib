package org.mesdag.portlib.diff.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.mesdag.portlib.diff.attachment.PortAttachmentSync;
import org.mesdag.portlib.event.PortEventHandler;
import org.mesdag.portlib.event.entity.player.PortPlayerRespawnPositionEvent;
import org.mesdag.portlib.wrapper.common.util.PortTriState;
import org.mesdag.portlib.wrapper.world.level.portal.PortDimensionTransition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
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

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @Inject(method = "respawn", at = @At(value = "NEW", target = "(Lnet/minecraft/server/MinecraftServer;Lnet/minecraft/server/level/ServerLevel;Lcom/mojang/authlib/GameProfile;)Lnet/minecraft/server/level/ServerPlayer;"))
    private void firePlayerRespawnPositionEvent(
            ServerPlayer player,
            boolean keepEverything,
            CallbackInfoReturnable<ServerPlayer> cir,
            @Local(name = "optional") Optional<Vec3> optional,
            @Local(name = "serverlevel1") ServerLevel serverlevel1,
            @Share("copyState") LocalRef<PortTriState> copyState,
            @Share("dimensiontransition") LocalRef<PortDimensionTransition> dimensiontransition
    ) {
        PortDimensionTransition transition = new PortDimensionTransition(serverlevel1, optional.isEmpty());
        PortPlayerRespawnPositionEvent event = new PortPlayerRespawnPositionEvent(player, transition, keepEverything, optional.isPresent());
        PortEventHandler.postEvent(event);
        if (event.copyOriginalSpawnPosition() == optional.isPresent()) {
            copyState.set(PortTriState.DEFAULT);
        } else {
            copyState.set(event.copyOriginalSpawnPosition() ? PortTriState.TRUE : PortTriState.FALSE);
        }
        dimensiontransition.set(event.getDimensionTransition());
    }

    @Inject(method = "respawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;setMainArm(Lnet/minecraft/world/entity/HumanoidArm;)V", shift = At.Shift.AFTER))
    private void copyOriginalSpawnPosition(
            CallbackInfoReturnable<ServerPlayer> cir,
            @Local(name = "blockpos") BlockPos blockpos,
            @Local(name = "f") float f,
            @Local(name = "flag") boolean flag,
            @Local(name = "serverlevel1") ServerLevel serverlevel1,
            @Local(name = "serverplayer") ServerPlayer serverplayer,
            @Share("copyState") LocalRef<PortTriState> copyState
    ) {
        if (copyState.get().isTrue()) {
            serverplayer.setRespawnPosition(serverlevel1.dimension(), blockpos, f, flag, false);
        }
    }

    @WrapWithCondition(method = "respawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;setRespawnPosition(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/core/BlockPos;FZZ)V"))
    private boolean cancelIfCopied(
            ServerPlayer instance, ResourceKey<Level> dimension, BlockPos position, float angle, boolean forced, boolean sendMessage,
            @Share("copyState") LocalRef<PortTriState> copyState
    ) {
        return !copyState.get().isTrue();
    }

    @ModifyVariable(method = "respawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"), name = "blockpos")
    private BlockPos blockpos(BlockPos blockpos, @Share("dimensiontransition") LocalRef<PortDimensionTransition> dimensiontransition) {
        return BlockPos.containing(dimensiontransition.get().pos().apply(Vec3.atBottomCenterOf(blockpos)));
    }

    @ModifyVariable(method = "respawn", at = @At("STORE"), name = "vec3")
    private Vec3 vec3(Vec3 vec3, @Share("dimensiontransition") LocalRef<PortDimensionTransition> dimensiontransition) {
        return dimensiontransition.get().pos().apply(vec3);
    }

    @WrapOperation(method = "respawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;moveTo(DDDFF)V"))
    private void modifyXyRot(
            ServerPlayer instance, double x, double y, double z,
            float yRot, float xRot,
            Operation<Void> original,
            @Share("dimensiontransition") LocalRef<PortDimensionTransition> dimensiontransition
    ) {
        original.call(instance, x, y, z, dimensiontransition.get().yRot().apply(yRot), dimensiontransition.get().xRot().apply(xRot));
    }

    // endregion PlayerRespawnPositionEvent
}
