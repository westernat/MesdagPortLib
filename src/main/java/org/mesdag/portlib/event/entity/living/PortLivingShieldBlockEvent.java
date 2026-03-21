package org.mesdag.portlib.event.entity.living;

import net.minecraft.world.damagesource.DamageSource;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.wrapper.common.damagesource.PortDamageContainer;

public class PortLivingShieldBlockEvent extends PortLivingEvent implements IPortCancellableEvent {
    private final LivingShieldBlockEvent e;

    @Diff
    public PortLivingShieldBlockEvent(LivingShieldBlockEvent e) {
        super(e.getEntity());
        this.e = e;
    }

    public PortDamageContainer getDamageContainer() {
        return PortDamageContainer.wrap(e.getDamageContainer());
    }

    public DamageSource getDamageSource() {
        return e.getDamageSource();
    }

    public float getOriginalBlockedDamage() {
        return e.getOriginalBlockedDamage();
    }

    public float getBlockedDamage() {
        return e.getBlockedDamage();
    }

    public float shieldDamage() {
        return e.shieldDamage();
    }

    public void setBlockedDamage(float blocked) {
        e.setBlockedDamage(blocked);
    }

    public void setShieldDamage(float damage) {
        e.setShieldDamage(damage);
    }

    public boolean getOriginalBlock() {
        return e.getOriginalBlock();
    }

    public boolean getBlocked() {
        return e.getBlocked();
    }

    public void setBlocked(boolean isBlocked) {
        e.setBlocked(isBlocked);
    }

    static {
        PortEventHooks.register(LivingShieldBlockEvent.class, PortLivingShieldBlockEvent.class, PortLivingShieldBlockEvent::new);
    }
}