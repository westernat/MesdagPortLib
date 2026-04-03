package org.mesdag.portlib.event.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortModBusEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.wrapper.world.entity.PortSpawnPlacementType;

public class PortRegisterSpawnPlacementsEvent extends PortEvent<RegisterSpawnPlacementsEvent> implements IPortModBusEvent {
    @Diff
    public PortRegisterSpawnPlacementsEvent(RegisterSpawnPlacementsEvent e) {
        super(e);
    }

    public <T extends Entity> void register(EntityType<T> entityType, SpawnPlacements.SpawnPredicate<T> predicate) {
        e.register(entityType, predicate);
    }

    public <T extends Entity> void register(EntityType<T> entityType, SpawnPlacements.SpawnPredicate<T> predicate, PortOperation operation) {
        e.register(entityType, predicate, operation.unwrap());
    }

    public <T extends Entity> void register(EntityType<T> entityType, @Nullable PortSpawnPlacementType placementType, @Nullable Heightmap.Types heightmap, SpawnPlacements.SpawnPredicate<T> predicate, PortOperation operation) {
        e.register(entityType, placementType == null ? null : placementType.unwrap(), heightmap, predicate, operation.unwrap());
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

    static {
        PortEventHooks.register(RegisterSpawnPlacementsEvent.class, PortRegisterSpawnPlacementsEvent.class, PortRegisterSpawnPlacementsEvent::new);
    }
}
