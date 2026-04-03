package org.mesdag.portlib.event.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortModBusEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.wrapper.world.entity.PortSpawnPlacementType;

public class PortRegisterSpawnPlacementsEvent extends PortEvent<SpawnPlacementRegisterEvent> implements IPortModBusEvent {
    @Diff
    public PortRegisterSpawnPlacementsEvent(SpawnPlacementRegisterEvent e) {
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
        public SpawnPlacementRegisterEvent.Operation unwrap() {
            return switch (this) {
                case AND -> SpawnPlacementRegisterEvent.Operation.AND;
                case OR -> SpawnPlacementRegisterEvent.Operation.OR;
                case REPLACE -> SpawnPlacementRegisterEvent.Operation.REPLACE;
            };
        }

        @Diff
        public static PortOperation wrap(SpawnPlacementRegisterEvent.Operation operation) {
            return switch (operation) {
                case AND -> AND;
                case OR -> OR;
                case REPLACE -> REPLACE;
            };
        }
    }

    static {
        PortEventHooks.register(SpawnPlacementRegisterEvent.class, PortRegisterSpawnPlacementsEvent.class, PortRegisterSpawnPlacementsEvent::new);
    }
}
