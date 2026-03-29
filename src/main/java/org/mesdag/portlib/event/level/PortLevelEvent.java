package org.mesdag.portlib.event.level;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.storage.ServerLevelData;
import net.neoforged.neoforge.event.level.LevelEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

import java.util.List;

public abstract class PortLevelEvent<E extends LevelEvent> extends PortEvent<E> {
    @Diff
    public PortLevelEvent(E e) {
        super(e);
    }

    public LevelAccessor getLevel() {
        return e.getLevel();
    }

    public static class PortLoad extends PortLevelEvent<LevelEvent.Load> {
        @Diff
        public PortLoad(LevelEvent.Load e) {
            super(e);
        }

        static {
            PortEventHooks.register(LevelEvent.Load.class, PortLoad.class, PortLoad::new);
        }
    }

    public static class PortUnload extends PortLevelEvent<LevelEvent.Unload> {
        @Diff
        public PortUnload(LevelEvent.Unload e) {
            super(e);
        }

        static {
            PortEventHooks.register(LevelEvent.Unload.class, PortUnload.class, PortUnload::new);
        }
    }

    public static class PortSave extends PortLevelEvent<LevelEvent.Save> {
        @Diff
        public PortSave(LevelEvent.Save e) {
            super(e);
        }

        static {
            PortEventHooks.register(LevelEvent.Save.class, PortSave.class, PortSave::new);
        }
    }

    public static class PortCreateSpawnPosition extends PortLevelEvent<LevelEvent.CreateSpawnPosition> implements IPortCancellableEvent {
        @Diff
        public PortCreateSpawnPosition(LevelEvent.CreateSpawnPosition e) {
            super(e);
        }

        public ServerLevelData getSettings() {
            return e.getSettings();
        }

        static {
            PortEventHooks.register(LevelEvent.CreateSpawnPosition.class, PortCreateSpawnPosition.class, PortCreateSpawnPosition::new);
        }
    }

    public static class PotentialSpawns extends PortLevelEvent<LevelEvent.PotentialSpawns> implements IPortCancellableEvent {
        @Diff
        public PotentialSpawns(LevelEvent.PotentialSpawns e) {
            super(e);
        }

        public MobCategory getMobCategory() {
            return e.getMobCategory();
        }

        public BlockPos getPos() {
            return e.getPos();
        }

        public List<MobSpawnSettings.SpawnerData> getSpawnerDataList() {
            return e.getSpawnerDataList();
        }

        public void addSpawnerData(MobSpawnSettings.SpawnerData data) {
            e.addSpawnerData(data);
        }

        public boolean removeSpawnerData(MobSpawnSettings.SpawnerData data) {
            return e.removeSpawnerData(data);
        }

        static {
            PortEventHooks.register(LevelEvent.PotentialSpawns.class, PotentialSpawns.class, PotentialSpawns::new);
        }
    }
}
