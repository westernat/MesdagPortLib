package org.mesdag.portlib.diff;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

@Diff
public interface IPortFoodProperties {
    float portlib$getEatSeconds();

    void portlib$setEatSeconds(float seconds);

    @Nullable ItemStack portlib$getUsingConvertsTo();

    void portlib$setUsingConvertsTo(@Nullable ItemStack stack);

    List<Pair<Supplier<MobEffectInstance>, Float>> portlib$getEffects();

    static IPortFoodProperties of(FoodProperties food) {
        return (IPortFoodProperties) food;
    }
}
