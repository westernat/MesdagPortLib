package PortLib.extensions.net.minecraft.world.food.FoodProperties;

import com.google.common.collect.Lists;
import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import org.mesdag.portlib.diff.IPortFoodProperties;
import org.mesdag.portlib.wrapper.world.food.PortFoodProperties;

import java.util.List;
import java.util.Optional;

@Extension
public class PortFoodPropertiesExtension {
    public static float getEatSeconds(@This FoodProperties thiz) {
        return IPortFoodProperties.of(thiz).portlib$getEatSeconds();
    }

    public static Optional<ItemStack> usingConvertsTo(@This FoodProperties thiz) {
        return Optional.ofNullable(IPortFoodProperties.of(thiz).portlib$getUsingConvertsTo());
    }

    public static List<PortFoodProperties.PortPossibleEffect> effects(@This FoodProperties thiz) {
        return Lists.transform(IPortFoodProperties.of(thiz).portlib$getEffects(), pair -> new PortFoodProperties.PortPossibleEffect(pair.getFirst(), pair.getSecond()));
    }
}
