package org.mesdag.portlib.event.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacementType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortModBusEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortRegisterSpawnPlacementsEvent extends PortEvent implements IPortModBusEvent {
    private final RegisterSpawnPlacementsEvent e;

    @Diff
    public PortRegisterSpawnPlacementsEvent(RegisterSpawnPlacementsEvent e) {
        this.e = e;
    }

    public <T extends Entity> void register(EntityType<T> entityType, SpawnPlacements.SpawnPredicate<T> predicate) {
        e.register(entityType, predicate);
    }

    public <T extends Entity> void register(EntityType<T> entityType, SpawnPlacements.SpawnPredicate<T> predicate, PortOperation operation) {
        e.register(entityType, predicate, operation.unwrap());
    }

    public <T extends Entity> void register(EntityType<T> entityType, @Nullable SpawnPlacementType placementType, @Nullable Heightmap.Types heightmap, SpawnPlacements.SpawnPredicate<T> predicate, PortOperation operation) {
        e.register(entityType, placementType, heightmap, predicate, operation.unwrap());
    }

    public enum PortOperation {
        AND,
        OR,
        REPLACE;

        @Diff
        public RegisterSpawnPlacementsEvent.Operation unwrap() {
            return switch (this) {
                case AND -> RegisterSpawnPlacementsEvent.Operation.AND;
                case OR -> RegisterSpawnPlacementsEvent.Operation.OR;
                case REPLACE -> RegisterSpawnPlacementsEvent.Operation.REPLACE;
            };
        }

        @Diff
        public static PortOperation wrap(RegisterSpawnPlacementsEvent.Operation operation) {
            return switch (operation) {
                case AND -> AND;
                case OR -> OR;
                case REPLACE -> REPLACE;
            };
        }
    }

    public static class PortMergedSpawnPredicate<T extends Entity> {
        private final RegisterSpawnPlacementsEvent.MergedSpawnPredicate<T> predicate;

        @Diff
        private PortMergedSpawnPredicate(RegisterSpawnPlacementsEvent.MergedSpawnPredicate<T> predicate) {
            this.predicate = predicate;
        }

        public SpawnPlacementType getSpawnType() {
            return predicate.getSpawnType();
        }

        public Heightmap.Types getHeightmapType() {
            return predicate.getHeightmapType();
        }

        @Diff
        public RegisterSpawnPlacementsEvent.MergedSpawnPredicate<T> unwrap() {
            return predicate;
        }

        @Diff
        public static <T extends Entity> PortMergedSpawnPredicate<T> wrap(RegisterSpawnPlacementsEvent.MergedSpawnPredicate<T> predicate) {
            return new PortMergedSpawnPredicate<>(predicate);
        }
    }

    static {
        PortEventHooks.register(RegisterSpawnPlacementsEvent.class, PortRegisterSpawnPlacementsEvent.class, PortRegisterSpawnPlacementsEvent::new);
    }
}
