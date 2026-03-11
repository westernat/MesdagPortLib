package org.mesdag.portlib.diff.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.extensions.IForgeItemStack;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.wrapper.PortItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = IForgeItemStack.class, remap = false)
public interface IForgeItemStackMixin {
    @Shadow
    private ItemStack self() {
        throw new UnsupportedOperationException();
    }

    @Inject(method = "getFoodProperties", at = @At("HEAD"), cancellable = true)
    private void getFood(@Nullable LivingEntity entity, CallbackInfoReturnable<FoodProperties> cir) {
        FoodProperties food = PortItemStack.getFood(self(), entity);
        if (food != null) {
            cir.setReturnValue(food);
        }
    }
}
