package org.mesdag.portlib.diff.mixin;

import net.minecraft.world.level.block.state.BlockState;
import org.mesdag.portlib.wrapper.common.extensions.IPortBlockStateExtension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockState.class)
public abstract class BlockStateMixin implements IPortBlockStateExtension {
}
