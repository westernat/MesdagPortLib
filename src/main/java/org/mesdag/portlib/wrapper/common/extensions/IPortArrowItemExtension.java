package org.mesdag.portlib.wrapper.common.extensions;

import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.mesdag.portlib.wrapper.world.item.PortProjectileItem;

import javax.annotation.Nullable;

public interface IPortArrowItemExtension extends PortProjectileItem {
    private ArrowItem self() {
        return (ArrowItem) this;
    }

    default AbstractArrow createArrow(Level level, ItemStack ammo, LivingEntity shooter, @Nullable ItemStack weapon) {
        AbstractArrow arrow = self().createArrow(level, ammo, shooter);
        IPortAbstractArrowExtension.of(arrow).setup(ammo.copyWithCount(1), weapon);
        return arrow;
    }

    default boolean isInfinite(ItemStack ammo, ItemStack bow, LivingEntity shooter) {
        return false;
    }

    @Override
    default Projectile asProjectile(Level level, Position pos, ItemStack stack, Direction direction) {
        Arrow arrow = new Arrow(level, pos.x(), pos.y(), pos.z());
        IPortAbstractArrowExtension.of(arrow).setup(stack.copyWithCount(1), null);
        arrow.pickup = AbstractArrow.Pickup.ALLOWED;
        return arrow;
    }
}
