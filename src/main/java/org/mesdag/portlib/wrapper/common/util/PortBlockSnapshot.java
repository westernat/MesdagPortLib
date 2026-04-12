package org.mesdag.portlib.wrapper.common.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.BlockSnapshot;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.diff.mixin.BlockSnapshotAccessor;

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
        return ((BlockSnapshotAccessor) delegate).getDim();
    }

    public BlockPos getPos() {
        return delegate.getPos();
    }

    public int getFlags() {
        return delegate.getFlag();
    }

    public @Nullable CompoundTag getTag() {
        return delegate.getTag();
    }

    public BlockState getState() {
        return delegate.getReplacedBlock();
    }

    public @Nullable LevelAccessor getLevel() {
        return delegate.getLevel();
    }

    public BlockState getCurrentState() {
        return delegate.getCurrentBlock();
    }

    public @Nullable BlockEntity recreateBlockEntity(HolderLookup.Provider provider) {
        return getTag() != null ? BlockEntity.loadStatic(getPos(), getState(), getTag()) : null;
    }

    public boolean restoreToLocation(LevelAccessor level, BlockPos pos, int flags) {
        BlockState replaced = getState();

        if (!level.setBlock(pos, replaced, flags)) {
            return false;
        }

        if (level instanceof Level realLevel) {
            BlockState current = getCurrentState();
            realLevel.sendBlockUpdated(pos, current, replaced, flags);
        }

        restoreBlockEntity(level, pos);

        return true;
    }

    public boolean restore(int flags) {
        return restoreToLocation(getLevel(), getPos(), flags);
    }

    public boolean restore() {
        return restore(getFlags());
    }

    public boolean restoreBlockEntity(LevelAccessor level, BlockPos pos) {
        BlockEntity be;
        if (getTag() != null) {
            be = level.getBlockEntity(pos);
            if (be != null) {
                be.load(getTag());
                be.setChanged();
                return true;
            }
        }
        return false;
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
