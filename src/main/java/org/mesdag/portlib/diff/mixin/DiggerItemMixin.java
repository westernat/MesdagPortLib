package org.mesdag.portlib.diff.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.mesdag.portlib.diff.IPortItem;
import org.mesdag.portlib.wrapper.common.extensions.IPortItemStackExtension;
import org.mesdag.portlib.wrapper.common.util.PortTriState;
import org.mesdag.portlib.wrapper.world.item.component.PortTool;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(DiggerItem.class)
public abstract class DiggerItemMixin {
    @ModifyArg(method = "mineBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;hurtAndBreak(ILnet/minecraft/world/entity/LivingEntity;Ljava/util/function/Consumer;)V"))
    private int modify(int original, @Local(argsOnly = true) ItemStack stack) {
        PortTool tool = IPortItemStackExtension.of(stack).getTool();
        if (tool != null) {
            return tool.damagePerBlock();
        }
        return original;
    }

    @ModifyReturnValue(method = "isCorrectToolForDrops(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/block/state/BlockState;)Z",at=@At("RETURN"),remap = false)
    private boolean withTool(boolean original, ItemStack stack, BlockState state) {
        PortTriState triState = IPortItem.isCorrectToolForDrops(stack, state);
        return original && !triState.isFalse();
    }
}
