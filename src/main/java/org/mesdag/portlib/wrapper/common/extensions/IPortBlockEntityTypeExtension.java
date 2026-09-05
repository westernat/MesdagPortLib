package org.mesdag.portlib.wrapper.common.extensions;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Collections;
import java.util.Set;

public interface IPortBlockEntityTypeExtension<T extends BlockEntity> {
    default Set<Block> getValidBlocks() {
        return Collections.unmodifiableSet(((BlockEntityType<?>) this).validBlocks);
    }
}
