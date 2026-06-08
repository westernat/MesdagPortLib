package org.mesdag.portlib.wrapper.world.entity.projectile;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

@FunctionalInterface
public interface PortProjectileDeflection {
    PortProjectileDeflection NONE = (projectile, entity, random) -> {
    };
    PortProjectileDeflection REVERSE = (projectile, entity, random) -> {
        float f = 170.0F + random.nextFloat() * 20.0F;
        projectile.setDeltaMovement(projectile.getDeltaMovement().scale(-0.5));
        projectile.setYRot(projectile.getYRot() + f);
        projectile.yRotO += f;
        projectile.hasImpulse = true;
    };
    PortProjectileDeflection AIM_DEFLECT = (projectile, entity, random) -> {
        if (entity != null) {
            Vec3 vec3 = entity.getLookAngle().normalize();
            projectile.setDeltaMovement(vec3);
            projectile.hasImpulse = true;
        }
    };
    PortProjectileDeflection MOMENTUM_DEFLECT = (projectile, entity, random) -> {
        if (entity != null) {
            Vec3 vec3 = entity.getDeltaMovement().normalize();
            projectile.setDeltaMovement(vec3);
            projectile.hasImpulse = true;
        }
    };

    void deflect(Projectile projectile, @Nullable Entity entity, RandomSource random);
}
