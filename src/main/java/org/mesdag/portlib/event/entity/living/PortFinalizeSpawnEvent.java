package org.mesdag.portlib.event.entity.living;

import com.mojang.datafixers.util.Either;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortFinalizeSpawnEvent extends PortMobSpawnEvent<FinalizeSpawnEvent> implements IPortCancellableEvent {
    @Diff
    public PortFinalizeSpawnEvent(FinalizeSpawnEvent e) {
        super(e);
    }

    public DifficultyInstance getDifficulty() {
        return e.getDifficulty();
    }

    public void setDifficulty(DifficultyInstance inst) {
        e.setDifficulty(inst);
    }

    public MobSpawnType getSpawnType() {
        return e.getSpawnType();
    }

    public @Nullable SpawnGroupData getSpawnData() {
        return e.getSpawnData();
    }

    public void setSpawnData(@Nullable SpawnGroupData data) {
        e.setSpawnData(data);
    }

    public @Nullable Either<BlockEntity, Entity> getSpawner() {
        return e.getSpawner();
    }

    public void setSpawnCancelled(boolean cancel) {
        e.setSpawnCancelled(cancel);
    }

    public boolean isSpawnCancelled() {
        return e.isSpawnCancelled();
    }

    static {
        PortEventHooks.register();
    }
}
