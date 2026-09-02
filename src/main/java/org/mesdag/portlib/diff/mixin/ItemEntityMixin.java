package org.mesdag.portlib.diff.mixin;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.mesdag.portlib.wrapper.common.extensions.IPortItemStackExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {
    @Shadow
    public abstract ItemStack getItem();

    @Inject(method = "fireImmune", at = @At("HEAD"), cancellable = true)
    private void fireResistant(CallbackInfoReturnable<Boolean> cir) {
        if (IPortItemStackExtension.of(getItem()).getFireResistant()) {
            cir.setReturnValue(true);
        }
    }
}
