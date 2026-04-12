package org.mesdag.portlib.wrapper.common.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.BlockSnapshot;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;

public class PortBlockSnapshot {
    private final BlockSnapshot delegate;

    private PortBlockSnapshot(BlockSnapshot delegate) {
        this.delegate = delegate;
    }

    @Diff
    public BlockSnapshot unwrap() {
        return delegate;
    }

    @Diff
    public static PortBlockSnapshot wrap(BlockSnapshot delegate) {
        return new PortBlockSnapshot(delegate);
    }

    public ResourceKey<Level> getDimension() {
        return delegate.getDimension();
    }

    public BlockPos getPos() {
        return delegate.getPos();
    }

    public int getFlags() {
        return delegate.getFlags();
    }

    public @Nullable CompoundTag getTag() {
        return delegate.getTag();
    }

    public BlockState getState() {
        return delegate.getState();
    }

    public @Nullable LevelAccessor getLevel() {
        return delegate.getLevel();
    }

    public BlockState getCurrentState() {
        return delegate.getCurrentState();
    }

    public @Nullable BlockEntity recreateBlockEntity(HolderLookup.Provider provider) {
        return delegate.recreateBlockEntity(provider);
    }

    public boolean restoreToLocation(LevelAccessor level, BlockPos pos, int flags) {
        return delegate.restoreToLocation(level, pos, flags);
    }

    public boolean restore(int flags) {
        return delegate.restore(flags);
    }

    public boolean restore() {
        return delegate.restore(getFlags());
    }

    public boolean restoreBlockEntity(LevelAccessor level, BlockPos pos) {
        return delegate.restoreBlockEntity(level, pos);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof PortBlockSnapshot p && p.delegate.equals(delegate));
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }

    @Override
    public String toString() {
        return delegate.toString();
    }
}
