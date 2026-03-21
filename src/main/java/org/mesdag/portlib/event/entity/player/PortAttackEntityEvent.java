package org.mesdag.portlib.event.entity.player;

import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortAttackEntityEvent extends PortPlayerEvent implements IPortCancellableEvent {
    private final Entity target;

    @Diff
    public PortAttackEntityEvent(AttackEntityEvent e) {
        super(e.getEntity());
        this.target = e.getTarget();
    }

    public Entity getTarget() {
        return target;
    }

    static {
        PortEventHooks.register(AttackEntityEvent.class, PortAttackEntityEvent.class, PortAttackEntityEvent::new);
    }
}
