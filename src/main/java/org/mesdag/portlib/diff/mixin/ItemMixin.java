package org.mesdag.portlib.diff.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Cancellable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.component.PortDataComponentMap;
import org.mesdag.portlib.component.PortDataComponentType;
import org.mesdag.portlib.diff.IPortFoodProperties;
import org.mesdag.portlib.diff.IPortItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

@Mixin(Item.class)
public abstract class ItemMixin implements IPortItem {
    @Unique
    private @Nullable Map<PortDataComponentType<?>, Optional<?>> portlib$prototype;

    @Override
    public <T> T get(PortDataComponentType<T> type) {
        if (portlib$prototype == null) return null;
        return (T) portlib$prototype.get(type);
    }

    @Override
    public Set<PortDataComponentType<?>> keySet() {
        if (portlib$prototype == null) return Set.of();
        return portlib$prototype.keySet();
    }

    @Inject(method = "<init>",at=@At("TAIL"))
    private void setup(Item.Properties properties, CallbackInfo ci) {
        Consumer<PortBuilder> consumer = IPortProperties.of(properties).portlib$get();
        if (consumer != null) {
            PortBuilder builder = new PortDataComponentMap.PortBuilder();
            consumer.accept(builder);
            this.portlib$prototype = builder.getMap();
        }
    }

    @WrapOperation(method = "getUseDuration", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getFoodProperties(Lnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/world/food/FoodProperties;"))
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
        private Consumer<PortDataComponentMap.PortBuilder> portlib$consumer;

        @Override
        public void portlib$set(Consumer<PortBuilder> consumer) {
            this.portlib$consumer = consumer;
        }

        @Override
        public @Nullable Consumer<PortBuilder> portlib$get() {
            return portlib$consumer;
        }
    }
}
