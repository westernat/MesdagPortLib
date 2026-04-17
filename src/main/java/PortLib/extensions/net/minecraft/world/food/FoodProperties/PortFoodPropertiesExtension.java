package PortLib.extensions.net.minecraft.world.food.FoodProperties;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import org.mesdag.portlib.util.PortLists;
import org.mesdag.portlib.wrapper.world.food.PortFoodProperties;

import java.util.List;
import java.util.Optional;

@Extension
public class PortFoodPropertiesExtension {
    public static float getEatSeconds(@This FoodProperties thiz) {
        return thiz.eatSeconds();
    }

    public static Optional<ItemStack> getUsingConvertsTo(@This FoodProperties thiz) {
        return thiz.usingConvertsTo();
    }

    public static List<PortFoodProperties.PortPossibleEffect> getEffects(@This FoodProperties thiz) {
        return PortLists.immutableTransform(thiz.effects(), PortFoodProperties.PortPossibleEffect::wrap);
    }
}
