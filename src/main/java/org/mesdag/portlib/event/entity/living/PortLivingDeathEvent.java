package org.mesdag.portlib.event.entity.living;

import net.minecraft.world.damagesource.DamageSource;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortLivingDeathEvent extends PortLivingEvent implements IPortCancellableEvent {
    private final LivingDeathEvent e;

    @Diff
    public PortLivingDeathEvent(LivingDeathEvent e) {
        super(e.getEntity());
        this.e = e;
    }

    public DamageSource getSource() {
        return e.getSource();
    }

    static {
        PortEventHooks.register(LivingDeathEvent.class, PortLivingDeathEvent.class, PortLivingDeathEvent::new);
    }
}
