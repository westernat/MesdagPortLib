package PortLib.extensions.net.minecraft.world.entity.projectile.AbstractArrow;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.mixin.AbstractArrowAccessor;

@Extension
public class PortAbstractArrowExtension {
    public static void setup(
            @This AbstractArrow thiz,
            ItemStack pickupItemStack,
            @Nullable ItemStack firedFromWeapon
    ) {
        // 不需要
    }

    public static ItemStack pickupItem(@This AbstractArrow thiz) {
        return ((AbstractArrowAccessor) thiz).callGetPickupItem();
    }

    public static ItemStack pickupItemStackOrigin(@This AbstractArrow thiz) {
        return thiz.getPickupItemStackOrigin();
    }

    public static ItemStack defaultPickupItem(@This AbstractArrow thiz) {
        return ((AbstractArrowAccessor) thiz).callGetDefaultPickupItem();
    }

    public static ItemStack weaponItem(@This AbstractArrow thiz) {
        return thiz.getWeaponItem();
    }
}
