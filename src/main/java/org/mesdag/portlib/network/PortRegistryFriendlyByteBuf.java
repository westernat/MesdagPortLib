package org.mesdag.portlib.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.PortEnvironment;

public class PortRegistryFriendlyByteBuf extends FriendlyByteBuf {
    private final RegistryAccess registryAccess;
    private final PortConnectionType connectionType;

    public PortRegistryFriendlyByteBuf(ByteBuf source, RegistryAccess registryAccess, PortConnectionType connectionType) {
        super(source);
        this.registryAccess = registryAccess;
        this.connectionType = connectionType;
    }

    public PortConnectionType getConnectionType() {
        return connectionType;
    }

    public RegistryAccess registryAccess() {
        return registryAccess;
    }

    @Deprecated
    @Diff
    /// @see FriendlyByteBuf#wrap
    public static PortRegistryFriendlyByteBuf wrap(FriendlyByteBuf buffer) {
        return new PortRegistryFriendlyByteBuf(buffer, PortEnvironment.registryAccess(), PortConnectionType.MODDED);
    }
}
