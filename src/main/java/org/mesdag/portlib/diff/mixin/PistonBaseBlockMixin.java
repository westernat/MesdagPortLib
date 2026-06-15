package org.mesdag.portlib.diff.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.mesdag.portlib.diff.IPortBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * 在活塞推动破坏方块时调用 IPortBlock.onDestroyedByPushReaction
 */
@Mixin(PistonBaseBlock.class)
public abstract class PistonBaseBlockMixin {

    @WrapOperation(
            method = "moveBlocks",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"
            )
    )
    private boolean portlib$onDestroyedByPushReaction(
            Level instance, BlockPos blockpos2, BlockState newState, int flags,
            Operation<Boolean> original,
            Level level, BlockPos pos, Direction facing, boolean extending
    ) {
        if (flags == 18) {
            BlockState currentState = level.getBlockState(blockpos2);
            if (currentState.getBlock() instanceof IPortBlock portBlock) {
                portBlock.onDestroyedByPushReaction(currentState, level, blockpos2, facing, level.getFluidState(blockpos2));
            }
        }
        return original.call(instance, blockpos2, newState, flags);
    }
}
