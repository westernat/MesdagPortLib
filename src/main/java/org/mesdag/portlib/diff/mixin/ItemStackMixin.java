package org.mesdag.portlib.diff.mixin;

import PortLib.extensions.net.minecraft.world.item.Item.PortItemExtension;
import PortLib.extensions.net.minecraft.world.item.ItemStack.PortItemStackExtension;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
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
import org.mesdag.portlib.diff.IPortItem;
import org.mesdag.portlib.diff.IPortItemStack;
import org.mesdag.portlib.diff.component.PortPatchedDataComponentMap;
import org.mesdag.portlib.event.PortEventHandler;
import org.mesdag.portlib.event.client.PortAddAttributeTooltipsEvent;
import org.mesdag.portlib.event.client.PortGatherSkippedAttributeTooltipsEvent;
import org.mesdag.portlib.event.entity.player.PortUseItemOnBlockEvent;
import org.mesdag.portlib.wrapper.PortEnvironment;
import org.mesdag.portlib.wrapper.common.util.PortAttributeTooltipContext;
import org.mesdag.portlib.wrapper.world.food.PortFoodProperties;
import org.mesdag.portlib.wrapper.world.item.PortItem;
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

    @Shadow(remap = false)
    @Final
    @Nullable
    private Holder.@Nullable Reference<Item> delegate;

    @Shadow
    public abstract Item getItem();

    @Unique
    private @Nullable FoodProperties portlib$food;
    @Unique
    private @Nullable PortTool portlib$tool;
    @Unique
    private PortPatchedDataComponentMap portlib$patch = PortPatchedDataComponentMap.EMPTY;

    @Inject(method = "forgeInit", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;gatherCapabilities(Ljava/util/function/Supplier;)V"), remap = false)
    private void init(CallbackInfo ci) {
        assert delegate != null;
        this.portlib$patch = new PortPatchedDataComponentMap(delegate.value());
        CompoundTag tag = IPortItem.of(getItem()).portlib$defaultTag();
        if (tag != null) {
            getOrCreateTag().merge(tag);
        }
    }

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
        if (!isAdvanced.isCreative() && !PortItemStackExtension.getShowTooltip(portlib$self())) {
            cir.setReturnValue(List.of());
        }
    }

    @Inject(method = "getTooltipLines", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;shouldShowInTooltip(ILnet/minecraft/world/item/ItemStack$TooltipPart;)Z", ordinal = 2))
    private void hideStoredEnchantmentsTooltip(Player player, TooltipFlag isAdvanced, CallbackInfoReturnable<List<Component>> cir, @Local(name = "list") List<Component> list) {
        if (PortItemStackExtension.getShowStoredEnchantmentsTooltip(portlib$self())) {
            ItemStack.appendEnchantmentNames(list, EnchantedBookItem.getEnchantments(portlib$self()));
        }
    }

    @Inject(method = "hasFoil", at = @At("HEAD"), cancellable = true)
    private void override(CallbackInfoReturnable<Boolean> cir) {
        if (PortItemStackExtension.getEnchantmentGlintOverride(portlib$self())) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "save", at = @At("HEAD"))
    private void save(CompoundTag compoundTag, CallbackInfoReturnable<CompoundTag> cir) {
        if (portlib$patch.isEmpty()) return;
        getOrCreateTag().put(DATA_COMPONENTS, portlib$patch.serializeNBT(PortEnvironment.registryAccess()));
    }

    @Inject(method = "<init>(Lnet/minecraft/nbt/CompoundTag;)V", at = @At("TAIL"))
    private void load1(CallbackInfo ci) {
        CompoundTag tag = getTag();
        if (tag != null) {
            portlib$patch.load(tag);
        }
    }

    @Inject(method = "setTag", at = @At("TAIL"))
    private void load2(@Nullable CompoundTag compoundTag, CallbackInfo ci) {
        if (compoundTag != null) {
            portlib$patch.load(compoundTag);
        }
    }

    @ModifyReturnValue(method = "copy", at = @At(value = "RETURN", ordinal = 1))
    private ItemStack load3(ItemStack original) {
        CompoundTag tag = original.getTag();
        if (tag != null) {
            IPortItemStack.of(original).portlib$patch().load(tag);
        }
        return original;
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
    private void itemBeforeUseBlock(UseOnContext p_41662_, CallbackInfoReturnable<InteractionResult> cir) {
        PortUseItemOnBlockEvent event = new PortUseItemOnBlockEvent(p_41662_, PortUseItemOnBlockEvent.PortUsePhase.ITEM_BEFORE_BLOCK);
        PortEventHandler.postEvent(event);
        if (event.isCanceled()) {
            cir.setReturnValue(event.getCancellationResult().result());
        }
    }

    // region AttributeModifier

    @ModifyArg(method = "getTooltipLines", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 8))
    private <E> E modifyPositive(E e, @Local(name = "entry") Map.Entry<Attribute, AttributeModifier> entry, @Local(argsOnly = true) TooltipFlag tooltipFlag) {
        return IPortAttribute.fromElement(e, entry.getKey(), entry.getValue(), tooltipFlag);
    }

    @ModifyArg(method = "getTooltipLines", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 9))
    private <E> E modifyNegative(E e, @Local(name = "entry") Map.Entry<Attribute, AttributeModifier> entry, @Local(argsOnly = true) TooltipFlag tooltipFlag) {
        return IPortAttribute.fromElement(e, entry.getKey(), entry.getValue(), tooltipFlag);
    }

    @ModifyExpressionValue(method = "getTooltipLines", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/EquipmentSlot;values()[Lnet/minecraft/world/entity/EquipmentSlot;"))
    private EquipmentSlot[] gatherSkippedAttributeTooltips(
            EquipmentSlot[] original,
            @Local(argsOnly = true) @Nullable Player player,
            @Local(argsOnly = true) TooltipFlag isAdvanced,
            @Share("ctx") LocalRef<PortAttributeTooltipContext> ctx,
            @Share("event") LocalRef<PortGatherSkippedAttributeTooltipsEvent> evt
    ) {
        var itemCtx = PortItem.PortTooltipContext.of(player == null ? null : player.level());
        var context = PortAttributeTooltipContext.of(player, itemCtx, isAdvanced);
        ctx.set(context);
        var event = new PortGatherSkippedAttributeTooltipsEvent(portlib$self(), context);
        PortEventHandler.postEvent(event);
        evt.set(event);
        if (event.isSkippingAll()) {
            return new EquipmentSlot[0];
        }
        return original;
    }

    @ModifyExpressionValue(method = "getTooltipLines", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getAttributeModifiers(Lnet/minecraft/world/entity/EquipmentSlot;)Lcom/google/common/collect/Multimap;"))
    private Multimap<Attribute, AttributeModifier> checkSkip(
            Multimap<Attribute, AttributeModifier> original,
            @Local(name = "equipmentslot") EquipmentSlot equipmentslot,
            @Share("event") LocalRef<PortGatherSkippedAttributeTooltipsEvent> evt
    ) {
        var event = evt.get();
        if (event.isSkipped(equipmentslot)) {
            return PortGatherSkippedAttributeTooltipsEvent.EMPTY;
        }
        if (event.hasSkippedIds()) {
            Multimap<Attribute, AttributeModifier> multimap = HashMultimap.create();
            original.asMap().forEach((k, v) -> v.forEach(m -> {
                if (!event.isSkipped(m)) {
                    multimap.put(k, m);
                }
            }));
            return multimap;
        }
        return original;
    }

    @SuppressWarnings("UnresolvedLocalCapture")
    @Inject(method = "getTooltipLines", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;hasTag()Z", ordinal = 1))
    private void addAttributeTooltips(
            CallbackInfoReturnable<List<Component>> cir,
            @Local List<Component> list,
            @Share("ctx") LocalRef<PortAttributeTooltipContext> ctx
    ) {
        if (ctx.get() != null) {
            PortEventHandler.postEvent(new PortAddAttributeTooltipsEvent(portlib$self(), list::add, ctx.get()));
        }
    }

    @ModifyReturnValue(method = "getAttributeModifiers", at = @At("RETURN"))
    private Multimap<Attribute, AttributeModifier> withNewDefaultAttributeModifiers(Multimap<Attribute, AttributeModifier> original, @Local(argsOnly = true) EquipmentSlot slot) {
        if (original.isEmpty()) {
            return PortItemExtension.getDefaultPortAttributeModifiers(getItem(), portlib$self()).getAttributeModifiers(slot);
        }
        return original;
    }

    // endregion AttributeModifier
}
