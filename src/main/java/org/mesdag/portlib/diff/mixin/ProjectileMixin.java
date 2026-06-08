package org.mesdag.portlib.diff.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.IPortProjectile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Projectile.class)
public abstract class ProjectileMixin implements IPortProjectile {
    @Shadow
    protected abstract void onHit(HitResult result);

    @Unique
    private @Nullable Entity portlib$lastDeflectedBy;

    @Override
    public void portlib$setLastDeflectedBy(@Nullable Entity entity) {
        this.portlib$lastDeflectedBy = entity;
    }

    @Override
    public @Nullable Entity portlib$getLastDeflectedBy() {
        return portlib$lastDeflectedBy;
    }

    @Override
    public void portlib$onHit(HitResult result) {
        onHit(result);
    }
}
