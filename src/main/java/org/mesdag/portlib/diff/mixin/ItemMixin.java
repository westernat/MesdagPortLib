package org.mesdag.portlib.diff.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Cancellable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.component.PortDataComponentType;
import org.mesdag.portlib.diff.IPortFoodProperties;
import org.mesdag.portlib.diff.IPortItem;
import org.mesdag.portlib.wrapper.common.extensions.IPortItemExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.Set;

@Mixin(Item.class)
public abstract class ItemMixin implements IPortItem, IPortItemExtension {
    @Shadow(remap = false)
    private Object renderProperties;
    @Unique
    private Map<PortDataComponentType<?>, Object> portlib$prototype = Map.of();

    @Override
    public Map<PortDataComponentType<?>, Object> portlib$getComponents() {
        return portlib$prototype;
    }

    @Override
    public void portlib$setComponents(Map<PortDataComponentType<?>, Object> map) {
        this.portlib$prototype = map;
    }

    @Override
    public void portlib$setRenderPropertiesInternal(Object properties) {
        this.renderProperties = properties;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T portlib$get(PortDataComponentType<T> type) {
        return (T) portlib$prototype.get(type);
    }

    @Override
    public Set<PortDataComponentType<?>> portlib$keySet() {
        return portlib$prototype.keySet();
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void setup(Item.Properties properties, CallbackInfo ci) {
        PortBuilder builder = IPortProperties.of(properties).portlib$get();
        if (builder != null) {
            this.portlib$prototype = builder.getMap();
        }
    }

    @WrapOperation(method = "getUseDuration", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getFoodProperties(Lnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/world/food/FoodProperties;", remap = false))
    private FoodProperties eatDurationTicks(ItemStack instance, LivingEntity living, Operation<FoodProperties> original, @Cancellable CallbackInfoReturnable<Integer> cir) {
        FoodProperties food = original.call(instance, living);
        float eatSeconds = IPortFoodProperties.of(food).portlib$getEatSeconds();
        if (eatSeconds > 0) {
            cir.setReturnValue((int) (eatSeconds * 20));
        }
        return food;
    }

    @Mixin(Item.Properties.class)
    public static abstract class PropertiesMixin implements IPortProperties {
        @Unique
        private @Nullable PortBuilder portlib$builder;

        @Override
        public void portlib$set(PortBuilder builder) {
            this.portlib$builder = builder;
        }

        @Override
        public @Nullable PortBuilder portlib$get() {
            return portlib$builder;
        }
    }
}
