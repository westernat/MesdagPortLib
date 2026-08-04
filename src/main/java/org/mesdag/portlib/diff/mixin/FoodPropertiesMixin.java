package org.mesdag.portlib.diff.mixin;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.IPortFoodProperties;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;
import java.util.function.Supplier;

@Mixin(FoodProperties.class)
public abstract class FoodPropertiesMixin implements IPortFoodProperties {
    @Shadow
    @Final
    private List<Pair<Supplier<MobEffectInstance>, Float>> effects;
    @Unique
    private float portlib$eatSeconds;
    @Unique
    private Supplier<@Nullable ItemStack> portlib$usingConvertsTo = () -> null;

    @Override
    public float portlib$getEatSeconds() {
        return portlib$eatSeconds;
    }

    @Override
    public void portlib$setEatSeconds(float seconds) {
        this.portlib$eatSeconds = seconds;
    }

    @Override
    public @Nullable ItemStack portlib$getUsingConvertsTo() {
        return portlib$usingConvertsTo.get();
    }

    @Override
    public void portlib$setUsingConvertsTo(@Nullable Supplier<@Nullable ItemStack> stack) {
        // 1.21 的默认语义是“不产生转换物品”。1.20 构建器可能用 null 表示未配置，
        // 因此在桥接层统一归一化，避免每个调用方都重复补空 Supplier。
        this.portlib$usingConvertsTo = stack == null ? () -> null : stack;
    }

    @Override
    public List<Pair<Supplier<MobEffectInstance>, Float>> portlib$getEffects() {
        return effects;
    }
}
