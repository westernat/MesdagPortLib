package org.mesdag.portlib.diff;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.wrapper.common.PortSoundActions;
import org.mesdag.portlib.wrapper.common.extensions.IPortFluidTypePropertiesExtension;
import org.mesdag.portlib.wrapper.fluids.PortFluidType;

public interface IPortFluidType extends IPortClientExtensionsSetter {
    @Contract(pure = true)
    @Nullable PortFluidType.DripstoneDripInfo portlib$getDripInfo();

    default boolean handleCauldronDrip(Fluid fluid, Level level, BlockPos cauldronPos) {
        if (fluid instanceof FlowingFluid flowing && fluid.isSource(flowing.getSource(false)) && portlib$getDripInfo() != null) {
            BlockState cauldronBlock = portlib$getDripInfo().filledCauldron().defaultBlockState();
            level.setBlockAndUpdate(cauldronPos, cauldronBlock);
            level.gameEvent(GameEvent.BLOCK_CHANGE, cauldronPos, GameEvent.Context.of(cauldronBlock));
            SoundEvent dripSound = ((FluidType) this).getSound(null, level, cauldronPos, PortSoundActions.CAULDRON_DRIP);
            if (dripSound != null) {
                level.playSound(null, cauldronPos, dripSound, SoundSource.BLOCKS, 2.0F, level.getRandom().nextFloat() * 0.1F + 0.9F);
            }
            return true;
        }
        return false;
    }

    static IPortFluidType of(FluidType type) {
        return (IPortFluidType) type;
    }

    interface IPortProperties extends IPortFluidTypePropertiesExtension {
        void portlib$setDripInfo(float chance, ParticleOptions dripParticle, Block cauldron, @Nullable SoundEvent fillSound);

        @Nullable PortFluidType.DripstoneDripInfo portlib$getDripInfo();

        static IPortProperties of(FluidType.Properties properties) {
            return (IPortProperties) (Object) properties;
        }
    }
}
