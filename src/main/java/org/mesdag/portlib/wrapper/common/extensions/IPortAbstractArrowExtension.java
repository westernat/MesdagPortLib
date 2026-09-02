package org.mesdag.portlib.wrapper.common.extensions;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.diff.IPortAbstractArrow;
import org.mesdag.portlib.diff.mixin.AbstractArrowAccessor;

@SuppressWarnings("all")
public interface IPortAbstractArrowExtension {
    private AbstractArrow self() {
        return (AbstractArrow) this;
    }

    @Diff
    default void setup(ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        IPortAbstractArrow iArrow = IPortAbstractArrow.of(self());
        iArrow.portlib$setPickupItem(pickupItemStack);

        self().setCustomName(IPortItemStackExtension.of(pickupItemStack).getCustomName());
        boolean intangible = IPortItemStackExtension.of(pickupItemStack).getIntangibleProjectile();
        IPortItemStackExtension.of(pickupItemStack).setIntangibleProjectile(false);
        if (intangible) {
            self().pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
        }

        if (/*arrow.getPierceLevel() == 0 && */firedFromWeapon != null && self().level() instanceof ServerLevel) {
            if (firedFromWeapon.isEmpty()) {
                throw new IllegalArgumentException("Invalid weapon firing an arrow");
            }

            iArrow.portlib$setFiredFromWeapon(firedFromWeapon);
            int i = EnchantmentHelper.getTagEnchantmentLevel(Enchantments.PIERCING, firedFromWeapon);
            if (i > 0) {
                self().setPierceLevel((byte) i);
            }

            // EnchantmentHelper.onProjectileSpawned(serverlevel, firedFromWeapon, this, item -> this.firedFromWeapon = null);
        }
    }

    default ItemStack pickupItem() {
        return pickupItemStackOrigin().copy();
    }

    default ItemStack pickupItemStackOrigin() {
        IPortAbstractArrow arrow = IPortAbstractArrow.of(self());
        ItemStack pickup = arrow.portlib$getPickupItem();
        if (pickup == null || pickup.isEmpty()) {
            pickup = defaultPickupItem();
            arrow.portlib$setPickupItem(pickup);
            pickup = arrow.portlib$getPickupItem();
        }
        return pickup == null ? ItemStack.EMPTY : pickup;
    }

    default ItemStack defaultPickupItem() {
        return ((AbstractArrowAccessor) self()).callGetPickupItem();
    }

    default @Nullable ItemStack weaponItem() {
        return IPortAbstractArrow.of(self()).portlib$getFiredFromWeapon();
    }

    static IPortAbstractArrowExtension of(AbstractArrow arrow) {
        return (IPortAbstractArrowExtension) arrow;
    }
}
