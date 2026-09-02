package org.mesdag.portlib.wrapper.common.extensions;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.ApiStatus;
import org.mesdag.portlib.util.Protected;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public interface IPortProjectileWeaponItemExtension {
    @Protected
    default void shoot(
            ServerLevel level,
            LivingEntity shooter,
            InteractionHand hand,
            ItemStack weapon,
            List<ItemStack> projectileItems,
            float velocity,
            float inaccuracy,
            boolean isCrit,
            @Nullable LivingEntity target
    ) {
        float f = 1/* todo EnchantmentHelper.processProjectileSpread(level, weapon, shooter, 0.0F)*/;
        float f1 = projectileItems.size() == 1 ? 0.0F : 2.0F * f / (float) (projectileItems.size() - 1);
        float f2 = (float) ((projectileItems.size() - 1) % 2) * f1 / 2.0F;
        float f3 = 1.0F;

        for (int i = 0; i < projectileItems.size(); i++) {
            ItemStack itemstack = projectileItems.get(i);
            if (!itemstack.isEmpty()) {
                float f4 = f2 + f3 * (float) ((i + 1) / 2) * f1;
                f3 = -f3;
                Projectile projectile = this.createProjectile(level, shooter, weapon, itemstack, isCrit);
                this.shootProjectile(shooter, projectile, i, velocity, inaccuracy, f4, target);
                level.addFreshEntity(projectile);
                IPortItemStackExtension.of(weapon).hurtAndBreak(getDurabilityUse(itemstack), shooter, IPortLivingEntityExtension.getSlotForHand(hand));
                if (weapon.isEmpty()) {
                    break;
                }
            }
        }
    }

    @Protected
    default int getDurabilityUse(ItemStack stack) {
        return 1;
    }

    default void shootProjectile(LivingEntity shooter, Projectile projectile, int index, float velocity, float inaccuracy, float angle, @Nullable LivingEntity target) {
        portlib$shootProjectile(shooter, projectile, index, velocity, inaccuracy, angle, target);
    }

    @ApiStatus.Internal
    default void portlib$shootProjectile(LivingEntity shooter, Projectile projectile, int index, float velocity, float inaccuracy, float angle, @Nullable LivingEntity target) {}

    @Protected
    default Projectile createProjectile(Level level, LivingEntity shooter, ItemStack weapon, ItemStack ammo, boolean isCrit) {
        IPortArrowItemExtension item = ammo.getItem() instanceof IPortArrowItemExtension extension ? extension : (IPortArrowItemExtension) Items.ARROW;
        AbstractArrow arrow = item.createArrow(level, ammo, shooter, weapon);
        if (isCrit) {
            arrow.setCritArrow(true);
        }
        return customArrow(arrow, ammo, weapon);
    }

    default AbstractArrow customArrow(AbstractArrow arrow, ItemStack projectileStack, ItemStack weaponStack) {
        return arrow;
    }

    // todo projectile
    default Predicate<ItemStack> getSupportedHeldProjectiles(ItemStack stack) {
        return ammo -> true;
    }

    // todo projectile
    default Predicate<ItemStack> getAllSupportedProjectiles(ItemStack stack) {
        return ammo -> true;
    }

    @Protected
    static List<ItemStack> draw(ItemStack weapon, ItemStack ammo, LivingEntity shooter) {
        if (ammo.isEmpty()) {
            return List.of();
        }
        int i = /* todo shooter.level() instanceof ServerLevel serverlevel ? EnchantmentHelper.processProjectileCount(serverlevel, weapon, shooter, 1) : */1;
        List<ItemStack> list = new ArrayList<>(i);
//        ItemStack copy = ammo.copy();

        for (int j = 0; j < i; j++) {
            ItemStack itemstack = useAmmo(weapon, ammo /*j == 0 ? ammo : copy*/, shooter, false /*j > 0*/);
            if (!itemstack.isEmpty()) {
                list.add(itemstack);
            }
        }

        return list;
    }

    @Protected
    static ItemStack useAmmo(ItemStack weapon, ItemStack ammo, LivingEntity shooter, boolean intangable) {
        // Neo: Adjust this check to respect ArrowItem#isInfinite, bypassing processAmmoUse if true.
        int i = !intangable && shooter.level() instanceof ServerLevel /*serverlevel*/ &&
                !(IPortLivingEntityExtension.of(shooter).hasInfiniteMaterials() || (ammo.getItem() instanceof IPortArrowItemExtension ai && ai.isInfinite(ammo, weapon, shooter)))
                ? 1/* todo EnchantmentHelper.processAmmoUse(serverlevel, weapon, ammo, 1)*/
                : 0;
        if (i > ammo.getCount()) {
            return ItemStack.EMPTY;
        } else if (i == 0) {
            ItemStack copy = ammo.copyWithCount(1);
            IPortItemStackExtension.of(copy).setIntangibleProjectile(true);
            return copy;
        }
        ItemStack itemstack = ammo.split(i);
        if (ammo.isEmpty() && shooter instanceof Player player) {
            player.getInventory().removeItem(ammo);
        }

        return itemstack;
    }
}
