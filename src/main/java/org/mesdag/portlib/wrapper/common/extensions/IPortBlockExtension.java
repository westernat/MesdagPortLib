package org.mesdag.portlib.wrapper.common.extensions;

import PortLib.extensions.net.minecraft.world.level.block.Block.PortBlockExtension;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import java.util.function.BiConsumer;

@SuppressWarnings("all")
public interface IPortBlockExtension {
    private Block self() {
        return (Block) this;
    }

    default boolean isEmpty(BlockState state) {
        return PortBlockExtension.isEmpty(self(), state);
    }

    default void onExplosionHit(BlockState state, Level level, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> dropConsumer) {
        if (state.isAir()) return;
        Block block = state.getBlock();
        boolean isPlayer = explosion.getIndirectSourceEntity() instanceof Player;
        if (state.canDropFromExplosion(level, pos, explosion) && level instanceof ServerLevel serverlevel) {
            BlockEntity blockEntity = state.hasBlockEntity() ? level.getBlockEntity(pos) : null;
            LootParams.Builder builder = new LootParams.Builder(serverlevel)
                    .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
                    .withParameter(LootContextParams.TOOL, ItemStack.EMPTY)
                    .withOptionalParameter(LootContextParams.BLOCK_ENTITY, blockEntity)
                    .withOptionalParameter(LootContextParams.THIS_ENTITY, explosion.getDirectSourceEntity());
            if (explosion.blockInteraction == Explosion.BlockInteraction.DESTROY_WITH_DECAY) {
                builder.withParameter(LootContextParams.EXPLOSION_RADIUS, explosion.radius);
            }

            state.spawnAfterBreak(serverlevel, pos, ItemStack.EMPTY, isPlayer);
            state.getDrops(builder).forEach(p_311752_ -> dropConsumer.accept(p_311752_, pos));
        }

        state.onBlockExploded(level, pos, explosion);
    }

    static IPortBlockExtension of(Block block) {
        return (IPortBlockExtension) block;
    }
}
