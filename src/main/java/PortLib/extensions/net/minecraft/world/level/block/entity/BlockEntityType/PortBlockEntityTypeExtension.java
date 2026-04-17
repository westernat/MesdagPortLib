package PortLib.extensions.net.minecraft.world.level.block.entity.BlockEntityType;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.mesdag.portlib.diff.mixin.BlockEntityTypeAccessor;

import java.util.Collections;
import java.util.Set;

@Extension
public class PortBlockEntityTypeExtension {
    public static Set<Block> getValidBlocks(@This BlockEntityType<?> type) {
        return Collections.unmodifiableSet(((BlockEntityTypeAccessor) type).getValidBlocks());
    }
}
