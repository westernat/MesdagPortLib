package org.mesdag.portlib.wrapper.world.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.LevelReader;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;

public interface PortSpawnPlacementType {
    boolean isSpawnPositionOk(LevelReader level, BlockPos pos, @Nullable EntityType<?> type);

//    default BlockPos adjustSpawnPosition(LevelReader level, BlockPos pos) {
//        return pos;
//    }

    @Diff
    SpawnPlacements.Type unwrap();

    @Diff
    static PortSpawnPlacementType wrap(SpawnPlacements.Type type) {
        return new PortSpawnPlacementType() {
            @Override
            public boolean isSpawnPositionOk(LevelReader level, BlockPos pos, @Nullable EntityType<?> entityType) {
                return type == null || type.canSpawnAt(level, pos, entityType);
            }

            @Override
            public SpawnPlacements.Type unwrap() {
                return type;
            }
        };
    }
}
