package org.mesdag.portlib.diff.mixin;

import net.minecraft.core.BlockPos;
import org.mesdag.portlib.wrapper.common.extensions.IPortBlockPosExtension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockPos.class)
public abstract class BlockPosMixin implements IPortBlockPosExtension {
}
