package org.mesdag.portlib.diff;

import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.wrapper.network.PortRegistryFriendlyByteBuf;

@Diff
public interface PortAttachmentSyncHandler<T> {
    default boolean sendToPlayer(IPortAttachmentHolder holder, ServerPlayer to) {
        return true;
    }

    void write(PortRegistryFriendlyByteBuf buf, T attachment, boolean initialSync);

    @Nullable T read(IPortAttachmentHolder holder, PortRegistryFriendlyByteBuf buf, @Nullable T previousValue);
}
