package org.mesdag.portlib.wrapper.common.extensions;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.mesdag.portlib.event.entity.player.PortPlayerEvent;

public interface IPortPlayerExtension extends IPortLivingEntityExtension {
    private Player self() {
        return (Player) this;
    }

    default boolean hasCorrectToolForDrops(BlockState state, Level level, BlockPos pos) {
        return PortPlayerEvent.HarvestCheck.doPlayerHarvestCheck(self(), state, level, pos, self().hasCorrectToolForDrops(state));
    }

    default double blockInteractionRange() {
        return self().getBlockReach();
    }

    default double entityInteractionRange() {
        return self().getEntityReach();
    }

    @Override
    default boolean hasInfiniteMaterials() {
        return self().getAbilities().instabuild;
    }

    default boolean canInteractWithBlock(BlockPos pos, double distance) {
        return new AABB(pos).distanceToSqr(self().getEyePosition()) < Mth.square(blockInteractionRange() + distance);
    }

    static IPortPlayerExtension of(Player player) {
        return (IPortPlayerExtension) player;
    }
}
