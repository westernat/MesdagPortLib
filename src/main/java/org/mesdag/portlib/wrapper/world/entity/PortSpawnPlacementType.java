package org.mesdag.portlib.wrapper.world.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacementType;
import net.minecraft.world.level.LevelReader;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;

public interface PortSpawnPlacementType {
    boolean isSpawnPositionOk(LevelReader level, BlockPos pos, @Nullable EntityType<?> type);

//    default BlockPos adjustSpawnPosition(LevelReader level, BlockPos pos) {
//        return pos;
//    }

    @Diff
    SpawnPlacementType unwrap();

    @Diff
    static PortSpawnPlacementType wrap(SpawnPlacementType type) {
        return new PortSpawnPlacementType() {
            @Override
            public boolean isSpawnPositionOk(LevelReader level, BlockPos pos, @Nullable EntityType<?> entityType) {
                return type.isSpawnPositionOk(level, pos, entityType);
            }

            @Override
            public SpawnPlacementType unwrap() {
                return type;
            }
        };
    }
}
