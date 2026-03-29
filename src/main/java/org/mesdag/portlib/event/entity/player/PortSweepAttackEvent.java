package org.mesdag.portlib.event.entity.player;

import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.event.entity.player.SweepAttackEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortSweepAttackEvent extends PortPlayerEvent<SweepAttackEvent> implements IPortCancellableEvent {
    @Diff
    public PortSweepAttackEvent(SweepAttackEvent e) {
        super(e);
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

    static {
        PortEventHooks.register(SweepAttackEvent.class, PortSweepAttackEvent.class, PortSweepAttackEvent::new);
    }
}
