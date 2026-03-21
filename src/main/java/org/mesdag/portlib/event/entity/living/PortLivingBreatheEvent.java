package org.mesdag.portlib.event.entity.living;

import net.neoforged.neoforge.event.entity.living.LivingBreatheEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;

public class PortLivingBreatheEvent extends PortLivingEvent {
    private final LivingBreatheEvent e;

    @Diff
    public PortLivingBreatheEvent(LivingBreatheEvent e) {
        super(e.getEntity());
        this.e = e;
    }

    public boolean canBreathe() {
        return e.canBreathe();
    }

    public void setCanBreathe(boolean canBreathe) {
        e.setCanBreathe(canBreathe);
    }

    public int getConsumeAirAmount() {
        return e.getConsumeAirAmount();
    }

    public void setConsumeAirAmount(int consumeAirAmount) {
        e.setConsumeAirAmount(consumeAirAmount);
    }

    public int getRefillAirAmount() {
        return e.getRefillAirAmount();
    }

    public void setRefillAirAmount(int refillAirAmount) {
        e.setRefillAirAmount(refillAirAmount);
    }

    static {
        PortEventHooks.register(LivingBreatheEvent.class, PortLivingBreatheEvent.class, PortLivingBreatheEvent::new);
    }
}
