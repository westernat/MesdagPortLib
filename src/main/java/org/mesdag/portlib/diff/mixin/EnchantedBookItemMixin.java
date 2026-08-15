package org.mesdag.portlib.diff.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import PortLib.extensions.net.minecraft.world.item.ItemStack.PortItemStackExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(EnchantedBookItem.class)
public abstract class EnchantedBookItemMixin {
    /**
     * 附魔书会在自身的附加提示阶段显示储存附魔。这里仅控制这一行内容，不能改写
     * {@code ItemStack#getTooltipLines} 的整个附加提示开关，否则附魔书或其他模组追加的说明也会被隐藏。
     */
    @WrapOperation(
            method = "appendHoverText",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;appendEnchantmentNames(Ljava/util/List;Lnet/minecraft/nbt/ListTag;)V"
            )
    )
    private void portlib$applyStoredEnchantmentsTooltipFlag(
            List<Component> tooltip,
            ListTag enchantments,
            Operation<Void> original,
            ItemStack stack
    ) {
        if (PortItemStackExtension.getShowStoredEnchantmentsTooltip(stack)) {
            original.call(tooltip, enchantments);
        }
    }
}
