package org.mesdag.portlib.diff.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.mesdag.portlib.diff.attachment.PortAttachmentSync;
import org.mesdag.portlib.event.entity.player.PortCanPlayerSleepEvent;
import org.mesdag.portlib.wrapper.PortSelfGetter;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin implements PortSelfGetter<ServerPlayer> {
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
}
