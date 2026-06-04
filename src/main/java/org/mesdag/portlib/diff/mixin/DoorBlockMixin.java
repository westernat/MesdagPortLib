package org.mesdag.portlib.diff.mixin;

import PortLib.extensions.net.minecraft.world.entity.player.Player.PortPlayerExtension;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DoorBlock.class)
public abstract class DoorBlockMixin {
    @ModifyExpressionValue(method = "playerWillDestroy", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isCreative()Z"))
    private boolean hasCorrectToolForDrops(
            boolean original,
            @Local(argsOnly = true) Level level,
            @Local(argsOnly = true) BlockPos pos,
            @Local(argsOnly = true) BlockState state,
            @Local(argsOnly = true) Player player
    ) {
        return player.isCreative() || !PortPlayerExtension.hasCorrectToolForDrops(player, state, level, pos);
    }
}
