package org.mesdag.portlib.diff.mixin;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.gen.Accessor;

@org.spongepowered.asm.mixin.Mixin(net.minecraftforge.common.util.BlockSnapshot.class)
public interface BlockSnapshotAccessor {
    @Accessor(remap = false)
    ResourceKey<Level> getDim();
}
