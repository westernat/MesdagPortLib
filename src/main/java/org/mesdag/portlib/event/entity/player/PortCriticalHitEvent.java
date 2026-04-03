package org.mesdag.portlib.event.entity.player;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;

public class PortCriticalHitEvent extends PortPlayerEvent<CriticalHitEvent> {
    @Diff
    public PortCriticalHitEvent(CriticalHitEvent e) {
        super(e);
    }

    public Entity getTarget() {
        return e.getTarget();
    }

    public float getDamageMultiplier() {
        return e.getDamageModifier();
    }

    public void setDamageMultiplier(float dmgMultiplier) {
        e.setDamageModifier(dmgMultiplier);
    }

    public boolean isCriticalHit() {
        return e.getResult() == Result.ALLOW || (e.isVanillaCritical() && e.getResult() == Result.DEFAULT);
    }

    public void setCriticalHit(boolean isCriticalHit) {
        e.setResult(Result.ALLOW);
    }

    public float getVanillaMultiplier() {
        return e.getOldDamageModifier();
    }

    public boolean isVanillaCritical() {
        return e.isVanillaCritical();
    }

    static {
        PortEventHooks.register(CriticalHitEvent.class, PortCriticalHitEvent.class, PortCriticalHitEvent::new);
    }
}
