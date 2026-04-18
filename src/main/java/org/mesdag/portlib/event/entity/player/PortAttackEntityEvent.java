package org.mesdag.portlib.event.entity.player;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortAttackEntityEvent extends PortPlayerEvent<AttackEntityEvent> implements IPortCancellableEvent {
    @Diff
    public PortAttackEntityEvent(AttackEntityEvent e) {
        super(e);
    }

    public Entity getTarget() {
        return e.getTarget();
    }

    static {
        PortEventHooks.register();
    }
}
