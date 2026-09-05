package org.mesdag.portlib.diff.mixin;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.mesdag.portlib.wrapper.common.extensions.IPortBlockEntityTypeExtension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockEntityType.class)
public abstract class BlockEntityTypeMixin<T extends BlockEntity> implements IPortBlockEntityTypeExtension<T> {}
