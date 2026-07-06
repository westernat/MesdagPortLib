package org.mesdag.portlib.diff.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Cancellable;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.world.level.block.PointedDripstoneBlock;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.mesdag.portlib.diff.IPortFluidType;
import org.mesdag.portlib.wrapper.fluids.PortFluidType;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PointedDripstoneBlock.class)
public class PointedDripstoneBlockMixin {
    @ModifyVariable(method = "maybeTransferFluid", at = @At("HEAD"), argsOnly = true)
    private static float skipCheck(float randChance, @Share("cachedRandChance") LocalFloatRef cachedRandChance) {
        cachedRandChance.set(randChance);
        return 0;
    }

    @ModifyVariable(method = "maybeTransferFluid", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/PointedDripstoneBlock;isStalactiteStartPos(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;)Z"), argsOnly = true)
    private static float realRandChance(float randChance, @Share("cachedRandChance") LocalFloatRef cachedRandChance) {
        return cachedRandChance.get();
    }

    @ModifyExpressionValue(method = "maybeTransferFluid", at = @At(value = "FIELD", target = "Lnet/minecraft/world/level/block/PointedDripstoneBlock$FluidInfo;fluid:Lnet/minecraft/world/level/material/Fluid;", opcode = Opcodes.GETFIELD))
    private static Fluid preventReturn(Fluid fluid, @Share("cachedFluid") LocalRef<Fluid> cachedFluid) {
        cachedFluid.set(fluid);
        if (fluid != Fluids.WATER && fluid != Fluids.LAVA) {
            return Fluids.LAVA;
        }
        return fluid;
    }

    // 不要改成 name = "f"：第二个 fstore 发生时，f 的局部变量表作用域还没开始。
    @ModifyVariable(method = "maybeTransferFluid", at = @At(value = "STORE", ordinal = 1), ordinal = 1)
    private static float realF(float f, @Share("cachedFluid") LocalRef<Fluid> cachedFluid, @Cancellable CallbackInfo ci) {
        Fluid fluid = cachedFluid.get();
        if (fluid != Fluids.WATER && fluid != Fluids.LAVA) {
            PortFluidType.DripstoneDripInfo dripInfo = IPortFluidType.of(fluid.getFluidType()).portlib$getDripInfo();
            if (dripInfo != null) {
                return dripInfo.chance();
            }
            ci.cancel();
        }
        return f;
    }

    // 生产环境的局部变量名不稳定，不要改成 name = "fluid"。
    @ModifyVariable(method = "maybeTransferFluid", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/PointedDripstoneBlock;findTip(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;IZ)Lnet/minecraft/core/BlockPos;"), ordinal = 0)
    private static Fluid realFluid(Fluid fluid, @Share("cachedFluid") LocalRef<Fluid> cachedFluid) {
        return cachedFluid.get();
    }
}
