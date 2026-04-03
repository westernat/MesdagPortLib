package org.mesdag.portlib.event.entity.living;

import com.mojang.datafixers.util.Either;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortFinalizeSpawnEvent extends PortMobSpawnEvent<MobSpawnEvent.FinalizeSpawn> implements IPortCancellableEvent {
    @Diff
    public PortFinalizeSpawnEvent(MobSpawnEvent.FinalizeSpawn e) {
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
        BaseSpawner spawner = e.getSpawner();
        if (spawner == null) return null;
        Entity entity = spawner.getSpawnerEntity();
        if (entity != null) {
            return Either.right(entity);
        }
        BlockEntity blockEntity = spawner.getSpawnerBlockEntity();
        if (blockEntity != null) {
            return Either.left(blockEntity);
        }
        return null;
    }

    public void setSpawnCancelled(boolean cancel) {
        e.setSpawnCancelled(cancel);
    }

    public boolean isSpawnCancelled() {
        return e.isSpawnCancelled();
    }

    static {
        PortEventHooks.register(MobSpawnEvent.FinalizeSpawn.class, PortFinalizeSpawnEvent.class, PortFinalizeSpawnEvent::new);
    }
}
