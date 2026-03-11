package org.mesdag.portlib.diff;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

@Diff
public interface IPortFoodProperties {
    float portlib$getEatSeconds();

    void portlib$setEatSeconds(float seconds);

    @Nullable ItemStack portlib$getUsingConvertsTo();

    void portlib$setUsingConvertsTo(@Nullable ItemStack stack);

    static IPortFoodProperties of(FoodProperties food) {
        return (IPortFoodProperties) food;
    }
}
