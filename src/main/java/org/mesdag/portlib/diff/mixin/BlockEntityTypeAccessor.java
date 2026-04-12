package org.mesdag.portlib.diff.mixin;

import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@org.spongepowered.asm.mixin.Mixin(net.minecraft.world.level.block.entity.BlockEntityType.class)
public interface BlockEntityTypeAccessor {
    @Accessor
    Set<Block> getValidBlocks();

    @Mutable
    @Accessor
    void setValidBlocks(Set<Block> validBlocks);
}
