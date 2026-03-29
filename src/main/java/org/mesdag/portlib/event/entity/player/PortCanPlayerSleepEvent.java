package org.mesdag.portlib.event.entity.player;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player.BedSleepingProblem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.entity.player.CanPlayerSleepEvent;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;

public class PortCanPlayerSleepEvent extends PortPlayerEvent<CanPlayerSleepEvent> {
    @Diff
    public PortCanPlayerSleepEvent(CanPlayerSleepEvent e) {
        super(e);
    }

    @Override
    public ServerPlayer getEntity() {
        return e.getEntity();
    }

    public Level getLevel() {
        return e.getLevel();
    }

    public BlockPos getPos() {
        return e.getPos();
    }

    public BlockState getState() {
        return e.getState();
    }

    public @Nullable BedSleepingProblem getProblem() {
        return e.getProblem();
    }

    public void setProblem(@Nullable BedSleepingProblem problem) {
        e.setProblem(problem);
    }

    public @Nullable BedSleepingProblem getVanillaProblem() {
        return e.getVanillaProblem();
    }

    static {
        PortEventHooks.register(CanPlayerSleepEvent.class, PortCanPlayerSleepEvent.class, PortCanPlayerSleepEvent::new);
    }
}
