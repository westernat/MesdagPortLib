package org.mesdag.portlib.diff.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.mesdag.portlib.wrapper.common.extensions.IPortBlockExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PistonBaseBlock.class)
public abstract class PistonBaseBlockMixin {
    @WrapOperation(method = "moveBlocks", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"))
    private boolean portlib$onDestroyedByPushReaction(
            Level instance, BlockPos pos, BlockState newState, int flags,
            Operation<Boolean> original,
            @Local(argsOnly = true) Direction facing
    ) {
        if (flags == 18) {
            BlockState currentState = instance.getBlockState(pos);
            IPortBlockExtension.of(currentState.getBlock()).onDestroyedByPushReaction(currentState, instance, pos, facing, instance.getFluidState(pos));
        }
        return original.call(instance, pos, newState, flags);
    }
}
