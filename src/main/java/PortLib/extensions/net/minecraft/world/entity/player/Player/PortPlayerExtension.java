package PortLib.extensions.net.minecraft.world.entity.player.Player;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.mesdag.portlib.event.entity.player.PortPlayerEvent;

public class PortPlayerExtension {
    public static boolean hasCorrectToolForDrops(Player thiz, BlockState state, Level level, BlockPos pos) {
        return PortPlayerEvent.PortHarvestCheck.doPlayerHarvestCheck(thiz, state, level, pos, thiz.hasCorrectToolForDrops(state));
    }

    public static double blockInteractionRange(Player thiz) {
        return thiz.getBlockReach();
    }

    public static double entityInteractionRange(Player thiz) {
        return thiz.getEntityReach();
    }
}
