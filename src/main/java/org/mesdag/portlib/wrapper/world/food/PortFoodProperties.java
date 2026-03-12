package org.mesdag.portlib.wrapper.world.food;

import com.google.common.collect.Lists;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import org.mesdag.portlib.diff.Diff;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@SuppressWarnings("all")
public class PortFoodProperties {
    public float eatSeconds(FoodProperties food) {
        return food.eatSeconds();
    }

    public Optional<ItemStack> usingConvertsTo(FoodProperties food) {
        return food.usingConvertsTo();
    }

    public List<PortPossibleEffect> effects(FoodProperties food) {
        return Lists.transform(food.effects(), PortPossibleEffect::wrap);
    }

    public record PortPossibleEffect(Supplier<MobEffectInstance> effectSupplier, float probability) {
        @Diff
        public static PortPossibleEffect wrap(FoodProperties.PossibleEffect effect) {
            return new PortPossibleEffect(effect.effectSupplier(), effect.probability());
        }

        @Diff
        public FoodProperties.PossibleEffect unwrap() {
            return new FoodProperties.PossibleEffect(effectSupplier, probability);
        }

        public MobEffectInstance effect() {
            return new MobEffectInstance(this.effectSupplier.get());
        }
    }
}
