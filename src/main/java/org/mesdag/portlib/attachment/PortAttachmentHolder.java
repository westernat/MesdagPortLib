package org.mesdag.portlib.attachment;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.mesdag.portlib.diff.CPortAttachmentHolder;

import javax.annotation.Nullable;

public class PortAttachmentHolder {
    private final CPortAttachmentHolder delegate;

    private PortAttachmentHolder(CPortAttachmentHolder delegate) {
        this.delegate = delegate;
    }

    public boolean hasAttachments() {
        return delegate.hasAttachments();
    }

    public boolean hasData(PortAttachmentType<?> type) {
        return delegate.hasData(type);
    }

    public <T> T getData(PortAttachmentType<T> type) {
        return delegate.getData(type);
    }

    public <T> @Nullable T getExistingDataOrNull(PortAttachmentType<T> type) {
        return delegate.getExistingDataOrNull(type);
    }

    public <T> @Nullable T setData(PortAttachmentType<T> type, T data) {
        return delegate.setData(type, data);
    }

    public <T> @Nullable T removeData(PortAttachmentType<T> type) {
        return delegate.removeData(type);
    }

    public static PortAttachmentHolder wrap(BlockEntity o) {
        return new PortAttachmentHolder(CPortAttachmentHolder.of(o));
    }

    public static PortAttachmentHolder wrap(Entity o) {
        return new PortAttachmentHolder(CPortAttachmentHolder.of(o));
    }

    public static PortAttachmentHolder wrap(Level o) {
        return new PortAttachmentHolder(CPortAttachmentHolder.of(o));
    }

    public static PortAttachmentHolder wrap(ChunkAccess o) {
        return new PortAttachmentHolder(CPortAttachmentHolder.of(o));
    }
}
