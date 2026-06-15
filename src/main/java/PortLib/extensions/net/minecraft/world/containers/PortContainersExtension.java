package PortLib.extensions.net.minecraft.world.containers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class PortContainersExtension {
    public static void dropContentsOnDestroy(BlockState state, BlockState newState, Level level, BlockPos pos) {
        if (!state.is(newState.getBlock())) {
            BlockEntity var5 = level.getBlockEntity(pos);
            if (var5 instanceof Container) {
                Container container = (Container)var5;
                Containers.dropContents(level, pos, container);
                level.updateNeighbourForOutputSignal(pos, state.getBlock());
            }
        }

    }
}
