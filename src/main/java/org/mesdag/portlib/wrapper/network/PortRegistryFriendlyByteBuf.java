package org.mesdag.portlib.wrapper.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import org.mesdag.portlib.network.PortConnectionType;

public class PortRegistryFriendlyByteBuf extends RegistryFriendlyByteBuf {
    public PortRegistryFriendlyByteBuf(ByteBuf source, RegistryAccess registryAccess, PortConnectionType connectionType) {
        super(source, registryAccess, connectionType.unwrap());
    }
}
