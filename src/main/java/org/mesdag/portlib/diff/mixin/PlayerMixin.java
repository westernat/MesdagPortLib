package org.mesdag.portlib.diff.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import org.mesdag.portlib.diff.IPortFoodProperties;
import org.mesdag.portlib.wrapper.PortSelfGetter;
import org.mesdag.portlib.wrapper.world.entity.player.PortPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import javax.annotation.Nullable;

@Mixin(Player.class)
public abstract class PlayerMixin implements PortSelfGetter<Player> {
    @Shadow
    public abstract Inventory getInventory();

    @Shadow
    @Nullable
    public abstract ItemEntity drop(ItemStack itemStack, boolean includeThrowerName);

    @ModifyReturnValue(method = "eat", at = @At("RETURN"))
    private ItemStack usingConvertsTo(ItemStack original, @Local(argsOnly = true) ItemStack food) {
        FoodProperties foodProperties = food.getFoodProperties(portlib$self());
        if (foodProperties != null) {
            ItemStack stack = IPortFoodProperties.of(foodProperties).portlib$getUsingConvertsTo();
            if (stack != null && !PortPlayer.hasInfiniteMaterials(portlib$self())) {
                if (original.isEmpty()) {
                    return stack.copy();
                }

                if (!portlib$self().level().isClientSide()) {
                    ItemStack container = stack.copy();
                    if (!getInventory().add(container)) {
                        drop(container, false);
                    }
                }
            }
        }
        return original;
    }
}
