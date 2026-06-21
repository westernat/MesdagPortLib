package org.mesdag.portlib.diff.mixin;

import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import org.mesdag.portlib.wrapper.common.extensions.IPortDoubleBlockHalfExtension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DoubleBlockHalf.class)
public abstract class DoubleBlockHalfMixin implements IPortDoubleBlockHalfExtension {
}
