package org.mesdag.portlib.event.entity.player;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player.BedSleepingProblem;
import net.minecraftforge.event.entity.living.LivingEvent;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHandler;

import java.util.Optional;

public class PortCanContinueSleepingEvent extends LivingEvent {
    protected final @Nullable BedSleepingProblem problem;
    protected boolean mayContinueSleeping;

    public PortCanContinueSleepingEvent(LivingEntity entity, @Nullable BedSleepingProblem problem) {
        super(entity);
        this.problem = problem;
        this.mayContinueSleeping = (problem == null);
    }

    public Optional<BlockPos> getSleepingPos() {
        return getEntity().getSleepingPos();
    }

    public @Nullable BedSleepingProblem getProblem() {
        return problem;
    }

    public boolean mayContinueSleeping() {
        return mayContinueSleeping;
    }

    public void setContinueSleeping(boolean sleeping) {
        mayContinueSleeping = sleeping;
    }

    @Diff
    public static boolean canEntityContinueSleeping(LivingEntity sleeper, @Nullable BedSleepingProblem problem) {
        return PortEventHandler.postEventWithReturn(new PortCanContinueSleepingEvent(sleeper, problem)).mayContinueSleeping();
    }
}
