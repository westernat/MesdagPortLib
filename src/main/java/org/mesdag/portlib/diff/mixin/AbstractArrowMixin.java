package org.mesdag.portlib.diff.mixin;

import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.IPortAbstractArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin implements IPortAbstractArrow {
    @Shadow
    protected abstract ItemStack getPickupItem();

    @Unique
    private ItemStack portlib$pickupItemStack;
    @Unique
    private @Nullable ItemStack portlib$firedFromWeapon;

    @Override
    public ItemStack portlib$getPickupItem() {
        if (portlib$pickupItemStack == null) {
            portlib$pickupItemStack = getPickupItem();
        }
        return portlib$pickupItemStack;
    }

    @Override
    public void portlib$setPickupItem(ItemStack stack) {
        this.portlib$pickupItemStack = stack;
    }

    @Override
    public @Nullable ItemStack portlib$getFiredFromWeapon() {
        return portlib$firedFromWeapon;
    }

    @Override
    public void portlib$setFiredFromWeapon(@Nullable ItemStack stack) {
        this.portlib$firedFromWeapon = stack;
    }
}
