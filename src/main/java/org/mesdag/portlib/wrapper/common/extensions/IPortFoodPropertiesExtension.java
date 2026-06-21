package org.mesdag.portlib.wrapper.common.extensions;

import PortLib.extensions.net.minecraft.world.food.FoodProperties.PortFoodPropertiesExtension;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import org.mesdag.portlib.wrapper.world.food.PortFoodProperties;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("all")
public interface IPortFoodPropertiesExtension {

    private FoodProperties self() {
        return (FoodProperties) this;
    }

    default float getEatSeconds() {
        return PortFoodPropertiesExtension.getEatSeconds(self());
    }

    default Optional<ItemStack> usingConvertsTo() {
        return PortFoodPropertiesExtension.usingConvertsTo(self());
    }

    default List<PortFoodProperties.PortPossibleEffect> effects() {
        return PortFoodPropertiesExtension.effects(self());
    }

    static IPortFoodPropertiesExtension of(FoodProperties foodProperties) {
        return (IPortFoodPropertiesExtension) foodProperties;
    }
}
