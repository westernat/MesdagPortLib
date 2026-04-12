package org.mesdag.portlib.event.level;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.level.ExplosionEvent;
import org.mesdag.portlib.diff.Diff;

import java.util.List;

public class PortExplosionKnockbackEvent extends ExplosionEvent {
    private final Entity entity;
    private Vec3 knockbackVelocity;

    @Diff
    public PortExplosionKnockbackEvent(Level level, Explosion explosion, Entity entity, Vec3 knockbackVelocity) {
        super(level, explosion);
        this.entity = entity;
        this.knockbackVelocity = knockbackVelocity;
    }

    public List<BlockPos> getAffectedBlocks() {
        return getExplosion().getToBlow();
    }

    public Entity getAffectedEntity() {
        return entity;
    }

    public Vec3 getKnockbackVelocity() {
        return knockbackVelocity;
    }

    public void setKnockbackVelocity(Vec3 newKnockbackVelocity) {
        this.knockbackVelocity = newKnockbackVelocity;
    }
}
