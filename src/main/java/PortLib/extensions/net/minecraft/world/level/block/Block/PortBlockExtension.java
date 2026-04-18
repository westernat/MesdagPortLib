package PortLib.extensions.net.minecraft.world.level.block.Block;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.mesdag.portlib.event.entity.player.PortPlayerEvent;

@Extension
public class PortBlockExtension {
    public static boolean canHarvestBlock(@This Block thiz, BlockState state, BlockGetter level, BlockPos pos, Player player) {
        return PortPlayerEvent.PortHarvestCheck.doPlayerHarvestCheck(player, state, level, pos, player.hasCorrectToolForDrops(state));
    }
}
