package org.mesdag.portlib.wrapper.common.extensions;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.IPortFluidType;

public interface IPortFluidTypePropertiesExtension {
    default FluidType.Properties addDripstoneDripping(float chance, ParticleOptions dripParticle, Block cauldron, @Nullable SoundEvent fillSound) {
        ((IPortFluidType.IPortProperties) this).portlib$setDripInfo(chance, dripParticle, cauldron, fillSound);
        return (FluidType.Properties) (Object) this;
    }
}
