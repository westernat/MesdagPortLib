package org.mesdag.portlib.attachment;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.attachment.AttachmentSyncHandler;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.network.PortRegistryFriendlyByteBuf;

public interface PortAttachmentSyncHandler<T> {
    default boolean sendToPlayer(IPortAttachmentHolder holder, ServerPlayer to) {
        return true;
    }

    void write(PortRegistryFriendlyByteBuf buf, T attachment, boolean initialSync);

    @Nullable T read(IPortAttachmentHolder holder, PortRegistryFriendlyByteBuf buf, @Nullable T previousValue);

    @Diff
    default AttachmentSyncHandler<T> unwrap() {
        return new AttachmentSyncHandler<>() {
            @Override
            public boolean sendToPlayer(IAttachmentHolder holder, ServerPlayer to) {
                return PortAttachmentSyncHandler.this.sendToPlayer(IPortAttachmentHolder.wrap(holder), to);
            }

            @Override
            public void write(RegistryFriendlyByteBuf buf, T attachment, boolean initialSync) {
                PortAttachmentSyncHandler.this.write(PortRegistryFriendlyByteBuf.wrap(buf), attachment, initialSync);
            }

            @Override
            public @Nullable T read(IAttachmentHolder holder, RegistryFriendlyByteBuf buf, @Nullable T previousValue) {
                return PortAttachmentSyncHandler.this.read(IPortAttachmentHolder.wrap(holder), PortRegistryFriendlyByteBuf.wrap(buf), previousValue);
            }
        };
    }

    @Diff
    static <T> PortAttachmentSyncHandler<T> wrap(AttachmentSyncHandler<T> delegate) {
        return new Delegate<>(delegate);
    }

    @Diff
    record Delegate<T>(AttachmentSyncHandler<T> delegate) implements PortAttachmentSyncHandler<T> {
        @Override
        public boolean sendToPlayer(IPortAttachmentHolder holder, ServerPlayer to) {
            return delegate.sendToPlayer(holder.unwrap(), to);
        }

        @Override
        public void write(PortRegistryFriendlyByteBuf buf, T attachment, boolean initialSync) {
            delegate.write(buf, attachment, initialSync);
        }

        @Override
        public @Nullable T read(IPortAttachmentHolder holder, PortRegistryFriendlyByteBuf buf, @Nullable T previousValue) {
            return delegate.read(holder.unwrap(), buf, previousValue);
        }

        @Override
        public AttachmentSyncHandler<T> unwrap() {
            return delegate;
        }
    }
}
