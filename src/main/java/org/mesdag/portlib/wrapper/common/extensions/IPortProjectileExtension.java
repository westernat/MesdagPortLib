package org.mesdag.portlib.wrapper.common.extensions;

import PortLib.extensions.net.minecraft.world.entity.Entity.PortEntityExtension;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.mesdag.portlib.diff.IPortEntity;
import org.mesdag.portlib.diff.IPortProjectile;
import org.mesdag.portlib.util.Protected;
import org.mesdag.portlib.wrapper.world.entity.projectile.PortProjectileDeflection;

import javax.annotation.Nullable;

public interface IPortProjectileExtension extends IPortEntityExtension {
    private Projectile self() {
        return (Projectile) this;
    }

    @Protected
    default PortProjectileDeflection hitTargetOrDeflectSelf(HitResult hitResult) {
        IPortProjectile self = IPortProjectile.of(self());
        if (hitResult.getType() == HitResult.Type.ENTITY) {
            EntityHitResult entityHitResult = (EntityHitResult) hitResult;
            Entity entity = entityHitResult.getEntity();
            PortProjectileDeflection deflection = IPortEntity.of(entity).deflection(self());
            if (deflection != PortProjectileDeflection.NONE) {
                if (entity != self.portlib$getLastDeflectedBy() && deflect(deflection, entity, self().getOwner(), false)) {
                    self.portlib$setLastDeflectedBy(entity);
                }

                return deflection;
            }
        }

        self.portlib$onHit(hitResult);
        return PortProjectileDeflection.NONE;
    }

    default boolean deflect(PortProjectileDeflection deflection, @Nullable Entity entity, @Nullable Entity owner, boolean deflectedByPlayer) {
        if (!self().level().isClientSide) {
            deflection.deflect(self(), entity, PortEntityExtension.getRandom(self()));
            self().setOwner(owner);
            onDeflection(entity, deflectedByPlayer);
        }

        return true;
    }

    @Protected
    default void onDeflection(@Nullable Entity entity, boolean deflectedByPlayer) {}
}
