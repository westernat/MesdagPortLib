package org.mesdag.portlib.wrapper.world.food;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import org.mesdag.portlib.diff.Diff;

import java.util.function.Supplier;

public class PortFoodProperties {
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
