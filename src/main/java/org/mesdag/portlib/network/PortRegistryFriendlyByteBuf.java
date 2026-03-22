package org.mesdag.portlib.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import org.mesdag.portlib.wrapper.core.PortRegistryAccess;

public class PortRegistryFriendlyByteBuf extends FriendlyByteBuf {
    private final PortRegistryAccess registryAccess;
    private final PortConnectionType connectionType;

    public PortRegistryFriendlyByteBuf(ByteBuf source, RegistryAccess registryAccess, PortConnectionType connectionType) {
        super(source);
        this.registryAccess = new PortRegistryAccess(registryAccess);
        this.connectionType = connectionType;
    }

    public PortConnectionType getConnectionType() {
        return connectionType;
    }

    public PortRegistryAccess registryAccess() {
        return registryAccess;
    }
}
