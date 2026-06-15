package org.mesdag.portlib.diff;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;

public interface IPortBlock extends IPortClientExtensionsSetter {
    static IPortBlock of(Block block) {
        return (IPortBlock) block;
    }

    /**
     * 在活塞推动导致方块被破坏时调用
     */
    default void onDestroyedByPushReaction(BlockState state, Level level, BlockPos pos, Direction pushDirection, FluidState fluid) {
        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 18);
        level.gameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Context.of(state));
    }
}
