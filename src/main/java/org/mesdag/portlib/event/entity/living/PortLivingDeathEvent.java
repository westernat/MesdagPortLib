package org.mesdag.portlib.event.entity.living;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortLivingDeathEvent extends PortLivingEvent<LivingDeathEvent> implements IPortCancellableEvent {
    @Diff
    public PortLivingDeathEvent(LivingDeathEvent e) {
        super(e);
    }

    public DamageSource getSource() {
        return e.getSource();
    }

    static {
        PortEventHooks.register();
    }
}
