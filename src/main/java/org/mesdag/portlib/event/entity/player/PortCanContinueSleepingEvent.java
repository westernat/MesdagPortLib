package org.mesdag.portlib.event.entity.player;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player.BedSleepingProblem;
import net.neoforged.neoforge.event.entity.player.CanContinueSleepingEvent;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.event.entity.living.PortLivingEvent;

import java.util.Optional;

public class PortCanContinueSleepingEvent extends PortLivingEvent<CanContinueSleepingEvent> {
    @Diff
    public PortCanContinueSleepingEvent(CanContinueSleepingEvent e) {
        super(e);
    }

    public Optional<BlockPos> getSleepingPos() {
        return e.getEntity().getSleepingPos();
    }

    public @Nullable BedSleepingProblem getProblem() {
        return e.getProblem();
    }

    public boolean mayContinueSleeping() {
        return e.mayContinueSleeping();
    }

    public void setContinueSleeping(boolean sleeping) {
        e.setContinueSleeping(sleeping);
    }

    static {
        PortEventHooks.register(CanContinueSleepingEvent.class, PortCanContinueSleepingEvent.class, PortCanContinueSleepingEvent::new);
    }
}
