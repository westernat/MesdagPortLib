package org.mesdag.portlib.wrapper.common.extensions;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.entity.PartEntity;

import javax.annotation.Nullable;

public interface IPortExplosionExtension {
    static DamageSource getDefaultDamageSource(Level level, Entity source) {
        return level.damageSources().explosion(source, getOwner(source));
    }

    private static @Nullable Entity getOwner(@Nullable Entity source) {
        if (source instanceof PartEntity<?> partEntity) {
            return partEntity.getParent();
        } else if (source instanceof OwnableEntity ownableEntity) {
            return ownableEntity.getOwner();
        } else if (source instanceof TraceableEntity traceableEntity) {
            return traceableEntity.getOwner();
        }
        return source;
    }
}
