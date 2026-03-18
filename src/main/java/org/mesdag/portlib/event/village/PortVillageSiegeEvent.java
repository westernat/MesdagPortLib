package org.mesdag.portlib.event.village;

import net.minecraft.world.entity.ai.village.VillageSiege;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.village.VillageSiegeEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortVillageSiegeEvent extends PortEvent implements ICancellableEvent {
    private final VillageSiegeEvent internal;

    public PortVillageSiegeEvent(VillageSiegeEvent internal) {
        this.internal = internal;
    }

    public VillageSiege getSiege() {
        return internal.getSiege();
    }

    public Level getLevel() {
        return internal.getLevel();
    }

    public Player getPlayer() {
        return internal.getPlayer();
    }

    public Vec3 getAttemptedSpawnPos() {
        return internal.getAttemptedSpawnPos();
    }

    static {
        PortEventHooks.register(VillageSiegeEvent.class, PortVillageSiegeEvent.class, PortVillageSiegeEvent::new);
    }
}
