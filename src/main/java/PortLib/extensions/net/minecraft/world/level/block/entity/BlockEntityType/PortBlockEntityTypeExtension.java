package PortLib.extensions.net.minecraft.world.level.block.entity.BlockEntityType;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.mesdag.portlib.diff.mixin.BlockEntityTypeAccessor;

import java.util.Collections;
import java.util.Set;

public class PortBlockEntityTypeExtension {
    public static Set<Block> getValidBlocks(BlockEntityType<?> type) {
        return Collections.unmodifiableSet(((BlockEntityTypeAccessor) type).getValidBlocks());
    }
}
