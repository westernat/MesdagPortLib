package org.mesdag.portlib.wrapper.world.entity;

import net.minecraft.world.entity.SpawnPlacementTypes;

public interface PortSpawnPlacementTypes {
    PortSpawnPlacementType NO_RESTRICTIONS = PortSpawnPlacementType.wrap(SpawnPlacementTypes.NO_RESTRICTIONS);
    PortSpawnPlacementType IN_WATER = PortSpawnPlacementType.wrap(SpawnPlacementTypes.IN_WATER);
    PortSpawnPlacementType IN_LAVA = PortSpawnPlacementType.wrap(SpawnPlacementTypes.IN_LAVA);
    PortSpawnPlacementType ON_GROUND = PortSpawnPlacementType.wrap(SpawnPlacementTypes.ON_GROUND);
}
