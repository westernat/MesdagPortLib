package org.mesdag.portlib.diff.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.IPortAttribute;
import org.mesdag.portlib.diff.IPortItemStack;
import org.mesdag.portlib.diff.component.PortPatchedDataComponentMap;
import org.mesdag.portlib.event.PortEventHandler;
import org.mesdag.portlib.event.entity.player.PortUseItemOnBlockEvent;
import org.mesdag.portlib.wrapper.PortEnvironment;
import org.mesdag.portlib.wrapper.world.food.PortFoodProperties;
import org.mesdag.portlib.wrapper.world.item.component.PortTool;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements IPortItemStack {
    @Shadow
    public abstract CompoundTag getOrCreateTag();

    @Shadow
    @Nullable
    public abstract CompoundTag getTag();

    @Shadow
    @Final
    @Deprecated
    @javax.annotation.Nullable
    private Item item;
    @Unique
    private @Nullable FoodProperties portlib$food;
    @Unique
    private @Nullable PortTool portlib$tool;
    @Unique
    private final PortPatchedDataComponentMap portlib$patch = new PortPatchedDataComponentMap(item);

    @Override
    public PortPatchedDataComponentMap portlib$patch() {
        return portlib$patch;
    }

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
        if (!isAdvanced.isCreative() && !portlib$self().getShowTooltip()) {
            cir.setReturnValue(List.of());
        }
    }

    @Inject(method = "getTooltipLines", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;shouldShowInTooltip(ILnet/minecraft/world/item/ItemStack$TooltipPart;)Z", ordinal = 2))
    private void hideStoredEnchantmentsTooltip(Player player, TooltipFlag isAdvanced, CallbackInfoReturnable<List<Component>> cir, @Local(name = "list") List<Component> list) {
        if (portlib$self().getShowStoredEnchantmentsTooltip()) {
            ItemStack.appendEnchantmentNames(list, EnchantedBookItem.getEnchantments(portlib$self()));
        }
    }

    @Inject(method = "hasFoil", at = @At("HEAD"), cancellable = true)
    private void override(CallbackInfoReturnable<Boolean> cir) {
        if (portlib$self().getEnchantmentGlintOverride()) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "save", at = @At("HEAD"))
    private void save(CompoundTag compoundTag, CallbackInfoReturnable<CompoundTag> cir) {
        if (portlib$patch.isEmpty()) return;
        compoundTag.put(DATA_COMPONENTS, portlib$patch.serializeNBT(PortEnvironment.registryAccess()));
    }

    @Inject(method = "<init>(Lnet/minecraft/nbt/CompoundTag;)V", at = @At("TAIL"))
    private void load(CompoundTag compoundTag, CallbackInfo ci) {
        if (compoundTag.contains(DATA_COMPONENTS, Tag.TAG_COMPOUND)) {
            portlib$patch.deserializeNBT(PortEnvironment.registryAccess(), compoundTag.getCompound(DATA_COMPONENTS));
        }
    }

    @Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
    private void itemAfterBlock(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        PortUseItemOnBlockEvent event = new PortUseItemOnBlockEvent(context, PortUseItemOnBlockEvent.PortUsePhase.ITEM_AFTER_BLOCK);
        PortEventHandler.postEvent(event);
        if (event.isCanceled()) {
            cir.setReturnValue(event.getCancellationResult().result());
        }
    }

    @Inject(method = "onItemUseFirst", at = @At("HEAD"), cancellable = true, remap = false)
    private void itemBeforeUseBlock(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        PortUseItemOnBlockEvent event = new PortUseItemOnBlockEvent(context, PortUseItemOnBlockEvent.PortUsePhase.ITEM_BEFORE_BLOCK);
        PortEventHandler.postEvent(event);
        if (event.isCanceled()) {
            cir.setReturnValue(event.getCancellationResult().result());
        }
    }

    // region AttributeModifier

    @ModifyArg(method = "getTooltipLines", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 8))
    private <E> E modifyPositive(E e, @Local(name = "entry") Map.Entry<Attribute, AttributeModifier> entry, @Local(argsOnly = true) TooltipFlag tooltipFlag) {
        return IPortAttribute.fromElement(e, entry.getKey(), entry.getValue(), tooltipFlag, true);
    }

    @ModifyArg(method = "getTooltipLines", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 9))
    private <E> E modifyNegative(E e, @Local(name = "entry") Map.Entry<Attribute, AttributeModifier> entry, @Local(argsOnly = true) TooltipFlag tooltipFlag) {
        return IPortAttribute.fromElement(e, entry.getKey(), entry.getValue(), tooltipFlag, false);
    }

    // endregion AttributeModifier
}
