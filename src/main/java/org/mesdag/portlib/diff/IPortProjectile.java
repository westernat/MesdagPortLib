package org.mesdag.portlib.diff;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.wrapper.common.extensions.IPortProjectileExtension;

public interface IPortProjectile extends IPortProjectileExtension {
    void portlib$setLastDeflectedBy(@Nullable Entity entity);

    @Nullable Entity portlib$getLastDeflectedBy();

    void portlib$onHit(HitResult result);

    static IPortProjectile of(Projectile projectile) {
        return (IPortProjectile) projectile;
    }
}
