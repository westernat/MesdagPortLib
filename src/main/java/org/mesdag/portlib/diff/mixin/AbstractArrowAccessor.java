package org.mesdag.portlib.diff.mixin;

import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.gen.Invoker;

@org.spongepowered.asm.mixin.Mixin(net.minecraft.world.entity.projectile.AbstractArrow.class)
public interface AbstractArrowAccessor {
    @Invoker
    ItemStack callGetPickupItem();
}
