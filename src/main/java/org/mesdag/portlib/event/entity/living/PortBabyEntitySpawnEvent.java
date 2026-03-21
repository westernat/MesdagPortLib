package org.mesdag.portlib.event.entity.living;

import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.living.BabyEntitySpawnEvent;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortBabyEntitySpawnEvent extends PortEvent implements IPortCancellableEvent {
    private final BabyEntitySpawnEvent e;

    @Diff
    public PortBabyEntitySpawnEvent(BabyEntitySpawnEvent e) {
        this.e = e;
    }

    public Mob getParentA() {
        return e.getParentA();
    }

    public Mob getParentB() {
        return e.getParentB();
    }

    public @Nullable Player getCausedByPlayer() {
        return e.getCausedByPlayer();
    }

    public @Nullable AgeableMob getChild() {
        return e.getChild();
    }

    public void setChild(AgeableMob proposedChild) {
        e.setChild(proposedChild);
    }

    static {
        PortEventHooks.register(BabyEntitySpawnEvent.class, PortBabyEntitySpawnEvent.class, PortBabyEntitySpawnEvent::new);
    }
}
