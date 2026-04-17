package org.mesdag.portlib.wrapper.world.entity.projectile;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.IPortAbstractArrow;
import org.mesdag.portlib.diff.mixin.AbstractArrowAccessor;

public class PortAbstractArrow {
    public static void setup(
            AbstractArrow arrow,
            ItemStack pickupItemStack,
            @Nullable ItemStack firedFromWeapon
    ) {
        IPortAbstractArrow iArrow = IPortAbstractArrow.of(arrow);
        iArrow.portlib$setPickupItem(pickupItemStack);

        arrow.setCustomName(pickupItemStack.getCustomName());
        boolean intangible = pickupItemStack.getIntangibleProjectile();
        pickupItemStack.setIntangibleProjectile(false);
        if (intangible) {
            arrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
        }

        if (/*arrow.getPierceLevel() == 0 && */firedFromWeapon != null && arrow.level() instanceof ServerLevel level) {
            if (firedFromWeapon.isEmpty()) {
                throw new IllegalArgumentException("Invalid weapon firing an arrow");
            }

            iArrow.portlib$setFiredFromWeapon(firedFromWeapon);
            int i = EnchantmentHelper.getTagEnchantmentLevel(Enchantments.PIERCING, firedFromWeapon);
            if (i > 0) {
                arrow.setPierceLevel((byte) i);
            }

            // EnchantmentHelper.onProjectileSpawned(serverlevel, firedFromWeapon, this, item -> this.firedFromWeapon = null);
        }
    }

    public static ItemStack getPickupItem(AbstractArrow arrow) {
        return getPickupItemStackOrigin(arrow).copy();
    }

    public static ItemStack getPickupItemStackOrigin(AbstractArrow arrow) {
        return IPortAbstractArrow.of(arrow).portlib$getPickupItem();
    }

    public static ItemStack getDefaultPickupItem(AbstractArrow arrow) {
        return ((AbstractArrowAccessor) arrow).callGetPickupItem();
    }

    public static ItemStack getWeaponItem(AbstractArrow arrow) {
        return IPortAbstractArrow.of(arrow).portlib$getFiredFromWeapon();
    }
}
