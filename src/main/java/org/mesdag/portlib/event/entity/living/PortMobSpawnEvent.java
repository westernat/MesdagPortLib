package org.mesdag.portlib.event.entity.living;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.ServerLevelAccessor;
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.event.entity.PortEntityEvent;

public abstract class PortMobSpawnEvent<E extends MobSpawnEvent> extends PortEntityEvent<E> {
    @Diff
    protected PortMobSpawnEvent(E e) {
        super(e);
    }

    @Override
    public Mob getEntity() {
        return e.getEntity();
    }

    public ServerLevelAccessor getLevel() {
        return e.getLevel();
    }

    public double getX() {
        return e.getX();
    }

    public double getY() {
        return e.getY();
    }

    public double getZ() {
        return e.getZ();
    }

    public static class PortSpawnPlacementCheck extends PortEvent<MobSpawnEvent.SpawnPlacementCheck> {
        @Diff
        public PortSpawnPlacementCheck(MobSpawnEvent.SpawnPlacementCheck e) {
            super(e);
        }

        public EntityType<?> getEntityType() {
            return e.getEntityType();
        }

        public ServerLevelAccessor getLevel() {
            return e.getLevel();
        }

        public MobSpawnType getSpawnType() {
            return e.getSpawnType();
        }

        public BlockPos getPos() {
            return e.getPos();
        }

        public RandomSource getRandom() {
            return e.getRandom();
        }

        public boolean getDefaultResult() {
            return e.getDefaultResult();
        }

        public void setPortResult(PortResult result) {
            e.setResult(result.unwrap());
        }

        public PortResult getPortResult() {
            return PortResult.wrap(e.getResult());
        }

        public boolean getPlacementCheckResult() {
            return e.getPlacementCheckResult();
        }

        public enum PortResult {
            SUCCEED,
            DEFAULT,
            FAIL;

            @Diff
            public MobSpawnEvent.SpawnPlacementCheck.Result unwrap() {
                return switch (this) {
                    case SUCCEED -> MobSpawnEvent.SpawnPlacementCheck.Result.SUCCEED;
                    case DEFAULT -> MobSpawnEvent.SpawnPlacementCheck.Result.DEFAULT;
                    case FAIL -> MobSpawnEvent.SpawnPlacementCheck.Result.FAIL;
                };
            }

            @Diff
            public static PortResult wrap(MobSpawnEvent.SpawnPlacementCheck.Result result) {
                return switch (result) {
                    case SUCCEED -> SUCCEED;
                    case DEFAULT -> DEFAULT;
                    case FAIL -> FAIL;
                };
            }
        }

        static {
            PortEventHooks.register(MobSpawnEvent.SpawnPlacementCheck.class, PortSpawnPlacementCheck.class, PortSpawnPlacementCheck::new);
        }
    }

    public static class PortPositionCheck extends PortMobSpawnEvent<MobSpawnEvent.PositionCheck> {
        @Diff
        public PortPositionCheck(MobSpawnEvent.PositionCheck e) {
            super(e);
        }

        public @Nullable BaseSpawner getSpawner() {
            return e.getSpawner();
        }

        public MobSpawnType getSpawnType() {
            return this.e.getSpawnType();
        }

        public void setPortResult(PortResult result) {
            e.setResult(result.unwrap());
        }

        public PortResult getPortResult() {
            return PortResult.wrap(e.getResult());
        }

        public enum PortResult {
            SUCCEED,
            DEFAULT,
            FAIL;

            @Diff
            public MobSpawnEvent.PositionCheck.Result unwrap() {
                return switch (this) {
                    case SUCCEED -> MobSpawnEvent.PositionCheck.Result.SUCCEED;
                    case DEFAULT -> MobSpawnEvent.PositionCheck.Result.DEFAULT;
                    case FAIL -> MobSpawnEvent.PositionCheck.Result.FAIL;
                };
            }

            @Diff
            public static PortResult wrap(MobSpawnEvent.PositionCheck.Result result) {
                return switch (result) {
                    case SUCCEED -> SUCCEED;
                    case DEFAULT -> DEFAULT;
                    case FAIL -> FAIL;
                };
            }
        }

        static {
            PortEventHooks.register(MobSpawnEvent.PositionCheck.class, PortPositionCheck.class, PortPositionCheck::new);
        }
    }
}
