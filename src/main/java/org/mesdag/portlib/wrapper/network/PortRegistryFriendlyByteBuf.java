package org.mesdag.portlib.wrapper.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.network.PortConnectionType;

public class PortRegistryFriendlyByteBuf extends RegistryFriendlyByteBuf {
    public PortRegistryFriendlyByteBuf(ByteBuf source, RegistryAccess registryAccess, PortConnectionType connectionType) {
        super(source, registryAccess, connectionType.unwrap());
    }

    @Diff
    public static PortRegistryFriendlyByteBuf wrap(RegistryFriendlyByteBuf delegate) {
        return new PortRegistryFriendlyByteBuf(delegate.unwrap(), delegate.registryAccess(), PortConnectionType.wrap(delegate.getConnectionType()));
    }
}
