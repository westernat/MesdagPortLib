package org.mesdag.portlib.event.entity.player;

import net.minecraftforge.event.entity.player.PlayerFlyableFallEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;

public class PortPlayerFlyableFallEvent extends PortPlayerEvent<PlayerFlyableFallEvent> {
    @Diff
    public PortPlayerFlyableFallEvent(PlayerFlyableFallEvent e) {
        super(e);
    }

    public float getDistance() {
        return e.getDistance();
    }

    public void setDistance(float distance) {
        e.setDistance(distance);
    }

    public float getMultiplier() {
        return e.getMultiplier();
    }

    public void setMultiplier(float multiplier) {
        e.setMultiplier(multiplier);
    }

    static {
        PortEventHooks.register();
    }
}
