package org.mesdag.portlib.wrapper.common.extensions;

import PortLib.extensions.net.minecraft.world.entity.projectile.AbstractArrow.PortAbstractArrowExtension;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;

@SuppressWarnings("all")
public interface IPortAbstractArrowExtension {

    private AbstractArrow self() {
        return (AbstractArrow) this;
    }

    @Diff
    default void setup(ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        PortAbstractArrowExtension.setup(self(), pickupItemStack, firedFromWeapon);
    }

    default ItemStack pickupItem() {
        return PortAbstractArrowExtension.pickupItem(self());
    }

    default ItemStack pickupItemStackOrigin() {
        return PortAbstractArrowExtension.pickupItemStackOrigin(self());
    }

    default ItemStack defaultPickupItem() {
        return PortAbstractArrowExtension.defaultPickupItem(self());
    }

    default @Nullable ItemStack weaponItem() {
        return PortAbstractArrowExtension.weaponItem(self());
    }

    static IPortAbstractArrowExtension of(AbstractArrow arrow) {
        return (IPortAbstractArrowExtension) arrow;
    }
}
