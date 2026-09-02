package org.mesdag.portlib.wrapper.common.extensions;

import com.google.common.collect.Lists;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import org.mesdag.portlib.diff.IPortFoodProperties;
import org.mesdag.portlib.wrapper.world.food.PortFoodProperties;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("all")
public interface IPortFoodPropertiesExtension {
    private FoodProperties self() {
        return (FoodProperties) this;
    }

    default float getEatSeconds() {
        return IPortFoodProperties.of(self()).portlib$getEatSeconds();
    }

    default Optional<ItemStack> usingConvertsTo() {
        return Optional.ofNullable(IPortFoodProperties.of(self()).portlib$getUsingConvertsTo());
    }

    default List<PortFoodProperties.PortPossibleEffect> effects() {
        return Lists.transform(IPortFoodProperties.of(self()).portlib$getEffects(), pair -> new PortFoodProperties.PortPossibleEffect(pair.getFirst(), pair.getSecond()));
    }

    static IPortFoodPropertiesExtension of(FoodProperties foodProperties) {
        return (IPortFoodPropertiesExtension) foodProperties;
    }
}
