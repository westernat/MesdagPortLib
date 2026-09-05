package org.mesdag.portlib.diff.mixin;

import net.minecraft.world.level.block.state.properties.BlockSetType;
import org.mesdag.portlib.wrapper.common.extensions.IPortBlockSetTypeExtension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockSetType.class)
public abstract class BlockSetTypeMixin implements IPortBlockSetTypeExtension {
}
