package org.mesdag.portlib.event.entity.player;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.player.Player.BedSleepingProblem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHandler;

/// 在startSleepInBed计算后post的事件
public class PortCanPlayerSleepEvent extends PlayerEvent {
    private final BlockPos pos;
    private final BlockState state;
    private final @Nullable BedSleepingProblem vanillaProblem;
    private @Nullable BedSleepingProblem problem;

    public PortCanPlayerSleepEvent(ServerPlayer player, BlockPos pos, @Nullable BedSleepingProblem problem) {
        super(player);
        this.pos = pos;
        this.state = player.level().getBlockState(pos);
        this.vanillaProblem = this.problem = problem;
    }

    @Override
    public ServerPlayer getEntity() {
        return (ServerPlayer) super.getEntity();
    }

    public Level getLevel() {
        return getEntity().level();
    }

    public BlockPos getPos() {
        return pos;
    }

    public BlockState getState() {
        return state;
    }

    public @Nullable BedSleepingProblem getProblem() {
        return this.problem;
    }

    public void setProblem(@Nullable BedSleepingProblem problem) {
        this.problem = problem;
    }

    public @Nullable BedSleepingProblem getVanillaProblem() {
        return vanillaProblem;
    }

    @Diff
    public static Either<BedSleepingProblem, Unit> canPlayerStartSleeping(ServerPlayer player, BlockPos pos, Either<BedSleepingProblem, Unit> vanillaResult) {
        PortCanPlayerSleepEvent event = new PortCanPlayerSleepEvent(player, pos, vanillaResult.left().orElse(null));
        PortEventHandler.postEvent(event);
        return event.getProblem() != null ? Either.left(event.getProblem()) : Either.right(Unit.INSTANCE);
    }
}
