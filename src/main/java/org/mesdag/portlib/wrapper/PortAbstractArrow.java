package org.mesdag.portlib.wrapper;

import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.mixin.AbstractArrowAccessor;

public class PortAbstractArrow {
    public static void setup(
            AbstractArrow arrow,
            ItemStack pickupItemStack,
            @Nullable ItemStack firedFromWeapon
    ) {
        // 不需要
    }

    public static ItemStack getPickupItem(AbstractArrow arrow) {
        return ((AbstractArrowAccessor) arrow).callGetPickupItem();
    }

    public static ItemStack getPickupItemStackOrigin(AbstractArrow arrow) {
        return arrow.getPickupItemStackOrigin();
    }

    public static ItemStack getDefaultPickupItem(AbstractArrow arrow) {
        return ((AbstractArrowAccessor) arrow).callGetDefaultPickupItem();
    }

    public static ItemStack getWeaponItem(AbstractArrow arrow) {
        return arrow.getWeaponItem();
    }
}
