package org.mesdag.portlib.event.level;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.level.ExplosionKnockbackEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

import java.util.List;

public class PortExplosionKnockbackEvent extends PortEvent<ExplosionKnockbackEvent> {
    @Diff
    public PortExplosionKnockbackEvent(ExplosionKnockbackEvent e) {
        super(e);
    }

    public List<BlockPos> getAffectedBlocks() {
        return e.getAffectedBlocks();
    }

    public Entity getAffectedEntity() {
        return e.getAffectedEntity();
    }

    public Vec3 getKnockbackVelocity() {
        return e.getKnockbackVelocity();
    }

    public void setKnockbackVelocity(Vec3 newKnockbackVelocity) {
        e.setKnockbackVelocity(newKnockbackVelocity);
    }

    static {
        PortEventHooks.register(ExplosionKnockbackEvent.class, PortExplosionKnockbackEvent.class, PortExplosionKnockbackEvent::new);
    }
}
