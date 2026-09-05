package org.mesdag.portlib.diff.mixin;

import net.minecraft.world.level.block.SoundType;
import org.mesdag.portlib.wrapper.common.extensions.IPortSoundTypeExtension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SoundType.class)
public abstract class SoundTypeMixin implements IPortSoundTypeExtension {
}
