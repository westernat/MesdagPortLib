package org.mesdag.portlib.event.entity;

import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEventHooks;


public class PortProjectileImpactEvent extends PortEntityEvent<ProjectileImpactEvent> implements IPortCancellableEvent {
    @Diff
    public PortProjectileImpactEvent(ProjectileImpactEvent e) {
        super(e);
    }

    public HitResult getRayTraceResult() {
        return e.getRayTraceResult();
    }

    public Projectile getProjectile() {
        return e.getProjectile();
    }

    static {
        PortEventHooks.register();
    }
}
