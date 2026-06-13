package org.mesdag.portlib.wrapper.common.extensions;

import PortLib.extensions.net.minecraft.world.entity.Entity.PortEntityExtension;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.util.Final;
import org.mesdag.portlib.wrapper.common.PortTags;
import org.mesdag.portlib.wrapper.world.entity.projectile.PortProjectileDeflection;

public interface IPortEntityExtension {
    private Entity self() {
        return (Entity) this;
    }

    default RandomSource getRandom() {
        return PortEntityExtension.getRandom(self());
    }

    default double getDefaultGravity() {
        return 0.0;
    }

    @Final
    default double getGravity() {
        return self().isNoGravity() ? 0.0 : getDefaultGravity();
    }

    default void applyGravity() {
        double d0 = getGravity();
        if (d0 != 0.0) {
            self().setDeltaMovement(self().getDeltaMovement().add(0.0, -d0, 0.0));
        }
    }

    default PortProjectileDeflection deflection(Projectile projectile) {
        return self().getType().is(PortTags.EntityTypes.DEFLECTS_PROJECTILES)
                ? PortProjectileDeflection.REVERSE : PortProjectileDeflection.NONE;
    }

    default @Nullable ItemStack getWeaponItem() {
        return null;
    }

    static IPortEntityExtension of(Entity entity) {
        return (IPortEntityExtension) entity;
    }
}
