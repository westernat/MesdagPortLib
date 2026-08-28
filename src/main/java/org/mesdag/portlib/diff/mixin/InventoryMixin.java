package org.mesdag.portlib.diff.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.core.NonNullList;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.mesdag.portlib.diff.action.ItemStack$hurtAndBreakAction;
import org.mesdag.portlib.event.PortEventHandler;
import org.mesdag.portlib.event.entity.living.PortArmorHurtEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.EnumMap;
import java.util.function.Consumer;

@Mixin(Inventory.class)
public abstract class InventoryMixin {
    @Shadow
    @Final
    public Player player;

    @Shadow
    @Final
    public NonNullList<ItemStack> armor;

    @Inject(method = "hurtArmor", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/NonNullList;get(I)Ljava/lang/Object;"))
    private void preCollect(
            CallbackInfo ci,
            @Local(name = "i") int i,
            @Share("actions") LocalRef<ItemStack$hurtAndBreakAction[]> actions
    ) {
        ItemStack$hurtAndBreakAction[] arr = actions.get();
        if (arr == null) {
            actions.set(arr = new ItemStack$hurtAndBreakAction[armor.size()]);
        }
        arr[i] = new ItemStack$hurtAndBreakAction();
    }

    @WrapOperation(method = "hurtArmor", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;hurtAndBreak(ILnet/minecraft/world/entity/LivingEntity;Ljava/util/function/Consumer;)V"))
    private <T extends LivingEntity> void postCollect(
            ItemStack instance,
            int amount,
            T entity,
            Consumer<T> onBroken,
            Operation<Void> original,
            @Local(name = "i") int i,
            @Share("actions") LocalRef<ItemStack$hurtAndBreakAction[]> actions
    ) {
        if (instance.isEmpty()) return;
        ItemStack$hurtAndBreakAction[] arr = actions.get();
        if (arr == null) return;
        arr[i].prepare(instance, amount, entity, onBroken, original);
    }

    @Inject(method = "hurtArmor", at = @At("TAIL"))
    private void onArmorHurt(
            CallbackInfo ci,
            @Local(argsOnly = true) DamageSource source,
            @Share("actions") LocalRef<ItemStack$hurtAndBreakAction[]> actions
    ) {
        ItemStack$hurtAndBreakAction[] arr = actions.get();
        if (arr == null) return;

        EnumMap<EquipmentSlot, PortArmorHurtEvent.ArmorEntry> armorMap = new EnumMap<>(EquipmentSlot.class);
        for (int i = 0; i < arr.length; i++) {
            ItemStack$hurtAndBreakAction action = arr[i];
            if (action == null || action.notPrepared()) continue;
            armorMap.put(EquipmentSlot.byTypeAndIndex(EquipmentSlot.Type.ARMOR, i), action.toEntry());
        }

        PortArmorHurtEvent event = PortEventHandler.postEventWithReturn(new PortArmorHurtEvent(armorMap, player, source));
        if (event.isCanceled()) return;
        event.getArmorMap().forEach((slot, entry) -> arr[slot.getIndex()].call(entry.armorItemStack, (int) entry.newDamage));
    }
}
