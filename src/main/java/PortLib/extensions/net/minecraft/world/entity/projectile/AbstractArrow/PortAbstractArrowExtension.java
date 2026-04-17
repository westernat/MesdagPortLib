package PortLib.extensions.net.minecraft.world.entity.projectile.AbstractArrow;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.IPortAbstractArrow;
import org.mesdag.portlib.diff.mixin.AbstractArrowAccessor;

@Extension
public class PortAbstractArrowExtension {
    public static void setup(
            @This AbstractArrow thiz,
            ItemStack pickupItemStack,
            @Nullable ItemStack firedFromWeapon
    ) {
        IPortAbstractArrow iArrow = IPortAbstractArrow.of(thiz);
        iArrow.portlib$setPickupItem(pickupItemStack);

        thiz.setCustomName(pickupItemStack.getCustomName());
        boolean intangible = pickupItemStack.getIntangibleProjectile();
        pickupItemStack.setIntangibleProjectile(false);
        if (intangible) {
            thiz.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
        }

        if (/*arrow.getPierceLevel() == 0 && */firedFromWeapon != null && thiz.level() instanceof ServerLevel) {
            if (firedFromWeapon.isEmpty()) {
                throw new IllegalArgumentException("Invalid weapon firing an arrow");
            }

            iArrow.portlib$setFiredFromWeapon(firedFromWeapon);
            int i = EnchantmentHelper.getTagEnchantmentLevel(Enchantments.PIERCING, firedFromWeapon);
            if (i > 0) {
                thiz.setPierceLevel((byte) i);
            }

            // EnchantmentHelper.onProjectileSpawned(serverlevel, firedFromWeapon, this, item -> this.firedFromWeapon = null);
        }
    }

    public static ItemStack pickupItem(@This AbstractArrow thiz) {
        return thiz.pickupItemStackOrigin().copy();
    }

    public static ItemStack pickupItemStackOrigin(@This AbstractArrow thiz) {
        return IPortAbstractArrow.of(thiz).portlib$getPickupItem();
    }

    public static ItemStack defaultPickupItem(@This AbstractArrow thiz) {
        return ((AbstractArrowAccessor) thiz).callGetPickupItem();
    }

    public static ItemStack weaponItem(@This AbstractArrow thiz) {
        return IPortAbstractArrow.of(thiz).portlib$getFiredFromWeapon();
    }
}
