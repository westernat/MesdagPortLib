package org.mesdag.portlib.event.entity.player;

import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.event.entity.player.SweepAttackEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortSweepAttackEvent extends PortPlayerEvent implements IPortCancellableEvent {
    private final SweepAttackEvent e;

    @Diff
    public PortSweepAttackEvent(SweepAttackEvent e) {
        super(e.getEntity());
        this.e = e;
    }

    public Entity getTarget() {
        return e.getTarget();
    }

    public boolean isVanillaSweep() {
        return e.isVanillaSweep();
    }

    public boolean isSweeping() {
        return e.isSweeping();
    }

    public void setSweeping(boolean sweep) {
        e.setSweeping(sweep);
    }

    @Override
    public void setCanceled(boolean canceled) {
        IPortCancellableEvent.super.setCanceled(canceled);
        e.setCanceled(canceled);
    }

    static {
        PortEventHooks.register(SweepAttackEvent.class, PortSweepAttackEvent.class, PortSweepAttackEvent::new);
    }
}
