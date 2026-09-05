package org.mesdag.portlib.diff.mixin;

import net.minecraft.core.particles.ParticleTypes;
import org.mesdag.portlib.wrapper.common.extensions.IPortParticleTypesExtension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ParticleTypes.class)
public abstract class ParticleTypesMixin implements IPortParticleTypesExtension {
}
