package org.mesdag.portlib.wrapper.common.extensions;

import PortLib.extensions.net.minecraft.world.entity.player.Player.PortPlayerExtension;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

@SuppressWarnings("all")
public interface IPortPlayerExtension extends IPortLivingEntityExtension {

    private Player self() {
        return (Player) this;
    }

    default boolean hasCorrectToolForDrops(BlockState state, Level level, BlockPos pos) {
        return PortPlayerExtension.hasCorrectToolForDrops(self(), state, level, pos);
    }

    default double blockInteractionRange() {
        return PortPlayerExtension.blockInteractionRange(self());
    }

    default double entityInteractionRange() {
        return PortPlayerExtension.entityInteractionRange(self());
    }

    @Override
    default boolean hasInfiniteMaterials() {
        return PortPlayerExtension.hasInfiniteMaterials(self());
    }

    static IPortPlayerExtension of(Player player) {
        return (IPortPlayerExtension) player;
    }
}
