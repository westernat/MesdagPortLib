package org.mesdag.portlib.wrapper.world.entity;

import net.minecraft.world.entity.SpawnPlacements;

public interface PortSpawnPlacementTypes {
    PortSpawnPlacementType NO_RESTRICTIONS = PortSpawnPlacementType.wrap(SpawnPlacements.Type.NO_RESTRICTIONS);
    PortSpawnPlacementType IN_WATER = PortSpawnPlacementType.wrap(SpawnPlacements.Type.IN_WATER);
    PortSpawnPlacementType IN_LAVA = PortSpawnPlacementType.wrap(SpawnPlacements.Type.IN_LAVA);
    PortSpawnPlacementType ON_GROUND = PortSpawnPlacementType.wrap(SpawnPlacements.Type.ON_GROUND);
}
