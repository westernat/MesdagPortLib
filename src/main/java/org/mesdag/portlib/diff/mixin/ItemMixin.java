package org.mesdag.portlib.diff.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Cancellable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.mesdag.portlib.diff.IPortFoodProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin {
    @WrapOperation(method = "getUseDuration", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getFoodProperties(Lnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/world/food/FoodProperties;"))
    private FoodProperties eatDurationTicks(ItemStack instance, LivingEntity living, Operation<FoodProperties> original, @Cancellable CallbackInfoReturnable<Integer> cir) {
        FoodProperties food = original.call(instance, living);
        float eatSeconds = IPortFoodProperties.of(food).portlib$getEatSeconds();
        if (eatSeconds > 0) {
            cir.setReturnValue((int) (eatSeconds * 20));
        }
        return food;
    }
}
