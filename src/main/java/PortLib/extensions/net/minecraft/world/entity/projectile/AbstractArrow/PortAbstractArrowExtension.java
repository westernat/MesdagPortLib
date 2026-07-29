package PortLib.extensions.net.minecraft.world.entity.projectile.AbstractArrow;

import PortLib.extensions.net.minecraft.world.item.ItemStack.PortItemStackExtension;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.IPortAbstractArrow;
import org.mesdag.portlib.diff.mixin.AbstractArrowAccessor;

public class PortAbstractArrowExtension {
    public static void setup(
            AbstractArrow thiz,
            ItemStack pickupItemStack,
            @Nullable ItemStack firedFromWeapon
    ) {
        IPortAbstractArrow iArrow = IPortAbstractArrow.of(thiz);
        iArrow.portlib$setPickupItem(pickupItemStack);

        thiz.setCustomName(PortItemStackExtension.getCustomName(pickupItemStack));
        boolean intangible = PortItemStackExtension.getIntangibleProjectile(pickupItemStack);
        PortItemStackExtension.setIntangibleProjectile(pickupItemStack, false);
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

    public static ItemStack pickupItem(AbstractArrow thiz) {
        return pickupItemStackOrigin(thiz).copy();
    }

    public static ItemStack pickupItemStackOrigin(AbstractArrow thiz) {
        IPortAbstractArrow arrow = IPortAbstractArrow.of(thiz);
        ItemStack pickup = arrow.portlib$getPickupItem();
        if (pickup == null || pickup.isEmpty()) {
            pickup = defaultPickupItem(thiz);
            arrow.portlib$setPickupItem(pickup);
            pickup = arrow.portlib$getPickupItem();
        }
        return pickup == null ? ItemStack.EMPTY : pickup;
    }

    public static ItemStack defaultPickupItem(AbstractArrow thiz) {
        return ((AbstractArrowAccessor) thiz).callGetPickupItem();
    }

    public static @Nullable ItemStack weaponItem(AbstractArrow thiz) {
        return IPortAbstractArrow.of(thiz).portlib$getFiredFromWeapon();
    }
}
