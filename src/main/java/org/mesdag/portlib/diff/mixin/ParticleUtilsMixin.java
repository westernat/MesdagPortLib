package org.mesdag.portlib.diff.mixin;

import net.minecraft.util.ParticleUtils;
import org.mesdag.portlib.wrapper.common.extensions.IPortParticleUtilsExtension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ParticleUtils.class)
public abstract class ParticleUtilsMixin implements IPortParticleUtilsExtension {
}
