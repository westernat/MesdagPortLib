package org.mesdag.portlib.diff.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.player.Player;
import org.mesdag.portlib.event.entity.player.PortCanPlayerSleepEvent;
import org.mesdag.portlib.wrapper.PortSelfGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin implements PortSelfGetter<ServerPlayer> {
    @ModifyReturnValue(method = "startSleepInBed", at = @At("RETURN"))
    private Either<Player.BedSleepingProblem, Unit> canPlayerStartSleeping(
            Either<Player.BedSleepingProblem, Unit> original,
            @Local(argsOnly = true) BlockPos at
    ) {
        return PortCanPlayerSleepEvent.canPlayerStartSleeping(portlib$self(), at, original);
    }
}
