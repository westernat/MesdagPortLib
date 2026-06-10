package org.mesdag.portlib.wrapper.common.extensions;

import PortLib.extensions.net.minecraft.world.entity.projectile.AbstractArrow.PortAbstractArrowExtension;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public interface IPortArrowItemExtension {
    private ArrowItem self() {
        return (ArrowItem) this;
    }

    default AbstractArrow createArrow(Level level, ItemStack ammo, LivingEntity shooter, @Nullable ItemStack weapon) {
        AbstractArrow arrow = self().createArrow(level, ammo, shooter);
        PortAbstractArrowExtension.setup(arrow, ammo.copyWithCount(1), weapon);
        return arrow;
    }

    default boolean isInfinite(ItemStack ammo, ItemStack bow, LivingEntity shooter) {
        return false;
    }
}
