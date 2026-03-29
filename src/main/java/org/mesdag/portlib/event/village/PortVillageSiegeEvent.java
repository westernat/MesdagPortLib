package org.mesdag.portlib.event.village;

import net.minecraft.world.entity.ai.village.VillageSiege;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.village.VillageSiegeEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortVillageSiegeEvent extends PortEvent<VillageSiegeEvent> implements IPortCancellableEvent {
    @Diff
    public PortVillageSiegeEvent(VillageSiegeEvent e) {
        super(e);
    }

    public VillageSiege getSiege() {
        return e.getSiege();
    }

    public Level getLevel() {
        return e.getLevel();
    }

    public Player getPlayer() {
        return e.getPlayer();
    }

    public Vec3 getAttemptedSpawnPos() {
        return e.getAttemptedSpawnPos();
    }

    static {
        PortEventHooks.register(VillageSiegeEvent.class, PortVillageSiegeEvent.class, PortVillageSiegeEvent::new);
    }
}
