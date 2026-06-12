package org.mesdag.portlib.wrapper.world.food;

import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.diff.IPortFoodProperties;

import java.util.function.Supplier;

public class PortFoodProperties {
    @Diff
    public static final String KEY = "portlib:food";

    @Diff
    public static FoodProperties load(CompoundTag data) {
        FoodProperties.Builder builder = new FoodProperties.Builder()
                .nutrition(Math.max(data.getInt("nutrition"), 0))
                .saturationMod(data.getFloat("saturation"));
        if (data.getBoolean("can_always_eat")) {
            builder.alwaysEat();
        }
        FoodProperties food = builder.build();
        IPortFoodProperties iFood = IPortFoodProperties.of(food);
        iFood.portlib$setEatSeconds(data.getFloat("eat_seconds"));
        if (data.contains("using_converts_to", Tag.TAG_COMPOUND)) {
            iFood.portlib$setUsingConvertsTo(ItemStack.of(data.getCompound("using_converts_to")));
        }
        if (data.contains("effects", Tag.TAG_LIST)) {
            for (Tag tag : data.getList("effects", Tag.TAG_COMPOUND)) {
                CompoundTag possibleEffect = (CompoundTag) tag;
                MobEffectInstance effect = MobEffectInstance.load(possibleEffect.getCompound("effect"));
                if (effect == null) continue;
                float probability = possibleEffect.contains("probability", Tag.TAG_ANY_NUMERIC)
                        ? Mth.clamp(possibleEffect.getFloat("probability"), 0, 1) : 1;
                builder.effect(() -> effect, probability);
            }
        }
        return food;
    }

    @Diff
    public static CompoundTag save(FoodProperties food) {
        CompoundTag data = new CompoundTag();
        data.putInt("nutrition", food.getNutrition());
        data.putFloat("saturation", food.getSaturationModifier());
        data.putBoolean("can_always_eat", food.canAlwaysEat());
        IPortFoodProperties iFood = IPortFoodProperties.of(food);
        data.putFloat("eat_seconds", iFood.portlib$getEatSeconds());
        ItemStack stack = iFood.portlib$getUsingConvertsTo();
        if (stack != null) {
            data.put("using_converts_to", stack.save(new CompoundTag()));
        }
        ListTag listTag = new ListTag();
        for (Pair<Supplier<MobEffectInstance>, Float> effect : IPortFoodProperties.of(food).portlib$getEffects()) {
            CompoundTag tag1 = new CompoundTag();
            CompoundTag tag = effect.getFirst().get().save(new CompoundTag());
            tag1.put("effect", tag);
            if (effect.getSecond() != 1) {
                tag1.putFloat("probability", effect.getSecond());
            }
            listTag.add(tag1);
        }
        data.put("effects", listTag);
        return data;
    }

    public record PortPossibleEffect(Supplier<MobEffectInstance> effectSupplier,
                                     float probability) {
        public MobEffectInstance effect() {
            return new MobEffectInstance(this.effectSupplier.get());
        }
    }
}
