package org.mesdag.portlib.diff.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.mesdag.portlib.event.PortEventHandler;
import org.mesdag.portlib.event.entity.player.PortPlayerEnchantItemEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(EnchantmentMenu.class)
public abstract class EnchantmentMenuMixin {
    // refmap in build gradle
    @SuppressWarnings("UnresolvedLocalCapture")
    @Inject(method = "lambda$clickMenuButton$1", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getAbilities()Lnet/minecraft/world/entity/player/Abilities;", remap = true), remap = false)
    private void postEvent(CallbackInfo ci, @Local(argsOnly = true) Player player, @Local(index = 8) ItemStack itemstack2, @Local(index = 9) List<EnchantmentInstance> list) {
        PortEventHandler.postEvent(new PortPlayerEnchantItemEvent(player, itemstack2, list));
    }
}
