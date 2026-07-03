package org.mesdag.portlib.wrapper.common.extensions;

import PortLib.extensions.net.minecraft.world.level.block.state.BlockState.PortBlockStateExtension;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.BiConsumer;

@SuppressWarnings("all")
public interface IPortBlockStateExtension {
    private BlockState self() {
        return (BlockState) (Object) this;
    }

    default boolean isEmpty() {
        return PortBlockStateExtension.isEmpty(self());
    }

    default void onExplosionHit(Level level, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> dropConsumer) {
        IPortBlockExtension.of(self().getBlock()).onExplosionHit(self(), level, pos, explosion, dropConsumer);
    }

    static IPortBlockStateExtension of(BlockState state) {
        return (IPortBlockStateExtension) (Object) state;
    }
}
