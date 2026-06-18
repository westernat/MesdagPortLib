package org.mesdag.portlib.wrapper.entity;

import org.mesdag.portlib.network.PortRegistryFriendlyByteBuf;

public interface IPortEntityWithComplexSpawn {
    void writeSpawnData(PortRegistryFriendlyByteBuf buffer);

    void readSpawnData(PortRegistryFriendlyByteBuf additionalData);
}
