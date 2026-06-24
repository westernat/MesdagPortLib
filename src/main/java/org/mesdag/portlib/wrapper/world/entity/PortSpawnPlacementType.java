package org.mesdag.portlib.wrapper.world.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.LevelReader;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// todo

/// 务必在静态初始化时创建
public abstract class PortSpawnPlacementType {
    private static final Map<String, SpawnPlacements.Type> TYPES = new ConcurrentHashMap<>();

    private final String name;

    public PortSpawnPlacementType(String name) {
        this.name = name;
        unwrap();
    }

    public String getName() {
        return name;
    }

    public abstract boolean isSpawnPositionOk(LevelReader level, BlockPos pos, @Nullable EntityType<?> type);

    public BlockPos adjustSpawnPosition(LevelReader level, BlockPos pos) {
        return pos;
    }

    @Diff
    public SpawnPlacements.Type unwrap() {
        return TYPES.computeIfAbsent(name, s -> {
            try {
                return SpawnPlacements.Type.valueOf(s);
            } catch (Exception e) {
                return SpawnPlacements.Type.create(s, this::isSpawnPositionOk);
            }
        });
    }

    @Diff
    static PortSpawnPlacementType wrap(SpawnPlacements.Type type) {
        return new PortSpawnPlacementType(type.name()) {
            @Override
            public boolean isSpawnPositionOk(LevelReader level, BlockPos pos, @Nullable EntityType<?> entityType) {
                return entityType == null || type.canSpawnAt(level, pos, entityType);
            }

            @Override
            public SpawnPlacements.Type unwrap() {
                return type;
            }
        };
    }
}
