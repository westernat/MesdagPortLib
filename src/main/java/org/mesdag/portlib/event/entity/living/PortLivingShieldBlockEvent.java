package org.mesdag.portlib.event.entity.living;

import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.Cancelable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.common.damagesource.PortDamageContainer;

@Cancelable
public class PortLivingShieldBlockEvent extends LivingEvent {
    private final PortDamageContainer container;
    private float dmgBlocked;
    private float shieldDamage = -1;
    private final boolean originalBlocked;
    private boolean newBlocked;

    @Diff
    public PortLivingShieldBlockEvent(LivingEntity blocker, PortDamageContainer container, boolean originalBlockedState) {
        super(blocker);
        this.container = container;
        this.dmgBlocked = container.getNewDamage();
        this.originalBlocked = originalBlockedState;
        this.newBlocked = originalBlockedState;
        this.shieldDamage = container.getNewDamage();
    }

    public PortDamageContainer getDamageContainer() {
        return container;
    }

    public DamageSource getDamageSource() {
        return container.getSource();
    }

    public float getOriginalBlockedDamage() {
        return container.getOriginalDamage();
    }

    public float getBlockedDamage() {
        return Math.min(dmgBlocked, container.getNewDamage());
    }

    public float shieldDamage() {
        if (newBlocked) {
            return shieldDamage >= 0 ? shieldDamage : getBlockedDamage();
        }
        return 0;
    }

    public void setBlockedDamage(float blocked) {
        this.dmgBlocked = Mth.clamp(blocked, 0, this.getOriginalBlockedDamage());
    }

    public void setShieldDamage(float damage) {
        this.shieldDamage = damage;
    }

    public boolean getOriginalBlock() {
        return originalBlocked;
    }

    public boolean getBlocked() {
        return newBlocked;
    }

    public void setBlocked(boolean isBlocked) {
        this.newBlocked = isBlocked;
    }
}
