package org.mesdag.portlib.event.entity.living;

import net.minecraft.world.damagesource.DamageSource;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.wrapper.common.damagesource.IPortReductionFunction;
import org.mesdag.portlib.wrapper.common.damagesource.PortDamageContainer;

public class PortLivingIncomingDamageEvent extends PortLivingEvent<LivingIncomingDamageEvent> implements IPortCancellableEvent {
    @Diff
    public PortLivingIncomingDamageEvent(LivingIncomingDamageEvent e) {
        super(e);
    }

    public PortDamageContainer getContainer() {
        return PortDamageContainer.wrap(e.getContainer());
    }

    public DamageSource getSource() {
        return e.getSource();
    }

    public float getAmount() {
        return e.getAmount();
    }

    public float getOriginalAmount() {
        return e.getOriginalAmount();
    }

    public void setAmount(float newDamage) {
        e.setAmount(newDamage);
    }

    public void addReductionModifier(PortDamageContainer.PortReduction type, IPortReductionFunction reductionFunc) {
        e.addReductionModifier(type.unwrap(), reductionFunc.unwrap());
    }

    public void setInvulnerabilityTicks(int ticks) {
        e.setInvulnerabilityTicks(ticks);
    }

    static {
        PortEventHooks.register();
    }
}
