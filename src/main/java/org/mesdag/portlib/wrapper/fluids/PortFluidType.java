package org.mesdag.portlib.wrapper.fluids;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.registries.PortDeferredBlock;

public class PortFluidType {
    public record DripstoneDripInfo(
            float chance,
            @Nullable ParticleOptions dripParticle,
            PortDeferredBlock<? extends Block> filledCauldron
    ) {}
}
