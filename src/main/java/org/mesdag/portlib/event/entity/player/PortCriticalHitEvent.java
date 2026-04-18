package org.mesdag.portlib.event.entity.player;

import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.event.entity.player.CriticalHitEvent;
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
        return e.getDamageMultiplier();
    }

    public void setDamageMultiplier(float dmgMultiplier) {
        e.setDamageMultiplier(dmgMultiplier);
    }

    public boolean isCriticalHit() {
        return e.isCriticalHit();
    }

    public void setCriticalHit(boolean isCriticalHit) {
        e.setCriticalHit(isCriticalHit);
    }

    public float getVanillaMultiplier() {
        return e.getVanillaMultiplier();
    }

    public boolean isVanillaCritical() {
        return e.isVanillaCritical();
    }

//    public void setDisableSweep(boolean disableSweep) {
//        e.setDisableSweep(disableSweep);
//    }
//
//    public boolean disableSweep() {
//        return e.disableSweep();
//    }

    static {
        PortEventHooks.register();
    }
}
