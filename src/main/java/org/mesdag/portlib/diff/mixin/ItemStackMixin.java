package org.mesdag.portlib.diff.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.IPortItemStack;
import org.mesdag.portlib.wrapper.PortFoodProperties;
import org.mesdag.portlib.wrapper.PortItemStack;
import org.mesdag.portlib.wrapper.PortTool;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements IPortItemStack {
    @Shadow
    public abstract CompoundTag getOrCreateTag();

    @Shadow
    @Nullable
    public abstract CompoundTag getTag();

    @Unique
    private @Nullable FoodProperties portlib$food;
    @Unique
    private @Nullable PortTool portlib$tool;

    @Override
    public @Nullable FoodProperties portlib$getFood(@Nullable LivingEntity living) {
        return portlib$food;
    }

    @Override
    public void portlib$setFood(@Nullable FoodProperties food, boolean encode) {
        this.portlib$food = food;
        if (encode) {
            if (food == null) {
                CompoundTag tag = getTag();
                if (tag != null) {
                    tag.remove(PortFoodProperties.KEY);
                }
            } else {
                getOrCreateTag().put(PortFoodProperties.KEY, PortFoodProperties.save(food));
            }
        }
    }

    @Override
    public @Nullable PortTool portlib$getTool() {
        return portlib$tool;
    }

    @Override
    public void portlib$setTool(@Nullable PortTool tool, boolean encode) {
        this.portlib$tool = tool;
        if (encode) {
            if (tool == null) {
                CompoundTag tag = getTag();
                if (tag != null) {
                    tag.remove(PortTool.KEY);
                }
            } else {
                getOrCreateTag().put(PortTool.KEY, tool.save());
            }
        }
    }

    @Inject(method = "getTooltipLines", at = @At("HEAD"), cancellable = true)
    private void hideTooltip(Player player, TooltipFlag isAdvanced, CallbackInfoReturnable<List<Component>> cir) {
        if (!isAdvanced.isCreative() && !PortItemStack.getShowTooltip(portlib$self())) {
            cir.setReturnValue(List.of());
        }
    }

    @Inject(method = "getTooltipLines", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;shouldShowInTooltip(ILnet/minecraft/world/item/ItemStack$TooltipPart;)Z", ordinal = 2))
    private void hideStoredEnchantmentsTooltip(Player player, TooltipFlag isAdvanced, CallbackInfoReturnable<List<Component>> cir, @Local(name = "list") List<Component> list) {
        if (PortItemStack.getShowStoredEnchantmentsTooltip(portlib$self())) {
            ItemStack.appendEnchantmentNames(list, EnchantedBookItem.getEnchantments(portlib$self()));
        }
    }

    @Inject(method = "hasFoil", at = @At("HEAD"), cancellable = true)
    private void override(CallbackInfoReturnable<Boolean> cir) {
        if (PortItemStack.getEnchantmentGlintOverride(portlib$self())) {
            cir.setReturnValue(true);
        }
    }
}
