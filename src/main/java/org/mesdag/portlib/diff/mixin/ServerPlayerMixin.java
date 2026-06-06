package org.mesdag.portlib.diff.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.mesdag.portlib.diff.IPortServerPlayer;
import org.mesdag.portlib.diff.attachment.PortAttachmentSync;
import org.mesdag.portlib.event.entity.player.PortCanPlayerSleepEvent;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin implements IPortServerPlayer {
    @Unique
    private Vec3 portlib$lastKnownClientMovement = Vec3.ZERO;

    @Override
    public Vec3 portlib$getKnownMovement() {
        return portlib$lastKnownClientMovement;
    }

    @Override
    public void portlib$setKnownMovement(Vec3 knownMovement) {
        this.portlib$lastKnownClientMovement = knownMovement;
    }

    @Inject(method = "changeDimension", at = @At(value = "FIELD", target = "Lnet/minecraft/server/level/ServerPlayer;lastSentFood:I", opcode = Opcodes.PUTFIELD, shift = At.Shift.AFTER))
    private void syncInitialPlayerAttachments(CallbackInfoReturnable<Entity> cir) {
        PortAttachmentSync.syncInitialPlayerAttachments(portlib$self());
    }

    @ModifyReturnValue(method = "startSleepInBed", at = @At("RETURN"))
    private Either<Player.BedSleepingProblem, Unit> canPlayerStartSleeping(
            Either<Player.BedSleepingProblem, Unit> original,
            @Local(argsOnly = true) BlockPos at
    ) {
//        if (!portlib$self().level().getBlockState(at).hasProperty(HorizontalDirectionalBlock.FACING)) {
//            original = Either.right(Unit.INSTANCE);
//        }
        return PortCanPlayerSleepEvent.canPlayerStartSleeping(portlib$self(), at, original);
    }

    @Inject(method = "startRiding", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;positionRider(Lnet/minecraft/world/entity/Entity;)V"))
    private void resetKnownMovement(CallbackInfoReturnable<Boolean> cir) {
        portlib$setKnownMovement(Vec3.ZERO);
    }
}
