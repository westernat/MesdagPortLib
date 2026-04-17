package org.mesdag.portlib.wrapper.world.level;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;

public class PortExplosionDamageCalculator extends ExplosionDamageCalculator {
    public boolean shouldDamageEntity(Explosion explosion, Entity entity) {
        return true;
    }

    public float getKnockbackMultiplier(Entity entity) {
        return 1.0F;
    }

    /// @return negative damage if not affected
    public float getEntityDamage(Explosion explosion, Entity entity) {
        return -1;
    }

    public float modifyEntityDamage(Explosion explosion, Entity entity, float originalDamage) {
        return originalDamage;
    }
}
