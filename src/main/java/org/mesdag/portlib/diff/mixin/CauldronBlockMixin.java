package org.mesdag.portlib.diff.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import org.mesdag.portlib.diff.IPortFluidType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CauldronBlock.class)
public abstract class CauldronBlockMixin {
    @Inject(method = "receiveStalactiteDrip", at = @At("HEAD"), cancellable = true)
    private void checkDripInfo(BlockState state, Level level, BlockPos pos, Fluid fluid, CallbackInfo ci) {
        if (IPortFluidType.of(fluid.getFluidType()).handleCauldronDrip(fluid, level, pos)) {
            ci.cancel();
        }
    }
}
