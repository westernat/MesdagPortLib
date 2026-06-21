package org.mesdag.portlib.event.level;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraftforge.event.level.LevelEvent;
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

    public static class Load extends PortLevelEvent<LevelEvent.Load> {
        @Diff
        public Load(LevelEvent.Load e) {
            super(e);
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class Unload extends PortLevelEvent<LevelEvent.Unload> {
        @Diff
        public Unload(LevelEvent.Unload e) {
            super(e);
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class Save extends PortLevelEvent<LevelEvent.Save> {
        @Diff
        public Save(LevelEvent.Save e) {
            super(e);
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class CreateSpawnPosition extends PortLevelEvent<LevelEvent.CreateSpawnPosition> implements IPortCancellableEvent {
        @Diff
        public CreateSpawnPosition(LevelEvent.CreateSpawnPosition e) {
            super(e);
        }

        public ServerLevelData getSettings() {
            return e.getSettings();
        }

        static {
            PortEventHooks.register();
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
            PortEventHooks.register();
        }
    }
}
