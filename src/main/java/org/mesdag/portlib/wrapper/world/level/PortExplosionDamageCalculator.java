package org.mesdag.portlib.wrapper.world.level;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import org.jetbrains.annotations.ApiStatus;

public class PortExplosionDamageCalculator extends ExplosionDamageCalculator {
    @ApiStatus.NonExtendable
    @Override
    public float getEntityDamageAmount(Explosion explosion, Entity entity) {
        float damage = getEntityDamage(explosion, entity);
        if (damage < 0) {
            damage = super.getEntityDamageAmount(explosion, entity);
        }
        return modifyEntityDamage(explosion, entity, damage);
    }

    /// @return negative damage if not affected
    public float getEntityDamage(Explosion explosion, Entity entity) {
        return -1;
    }

    public float modifyEntityDamage(Explosion explosion, Entity entity, float originalDamage) {
        return originalDamage;
    }
}
