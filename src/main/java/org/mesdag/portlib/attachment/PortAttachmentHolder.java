package org.mesdag.portlib.attachment;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.mesdag.portlib.diff.CPortAttachmentHolder;

import javax.annotation.Nullable;
import java.util.function.Supplier;

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

    public <T> boolean hasData(Supplier<PortAttachmentType<T>> type) {
        return delegate.hasData(type);
    }

    public <T> T getData(PortAttachmentType<T> type) {
        return delegate.getData(type);
    }

    public <T> T getData(Supplier<PortAttachmentType<T>> type) {
        return delegate.getData(type);
    }

    public <T> @Nullable T getExistingDataOrNull(PortAttachmentType<T> type) {
        return delegate.getExistingDataOrNull(type);
    }

    public <T> @Nullable T getExistingDataOrNull(Supplier<PortAttachmentType<T>> type) {
        return delegate.getExistingDataOrNull(type.get());
    }

    public <T> @Nullable T setData(PortAttachmentType<T> type, T data) {
        return delegate.setData(type, data);
    }

    public <T> @Nullable T setData(Supplier<PortAttachmentType<T>> type, T data) {
        return delegate.setData(type, data);
    }

    public <T> @Nullable T removeData(PortAttachmentType<T> type) {
        return delegate.removeData(type);
    }

    public <T> @Nullable T removeData(Supplier<PortAttachmentType<T>> type) {
        return delegate.removeData(type);
    }

    public static PortAttachmentHolder of(BlockEntity o) {
        return new PortAttachmentHolder(CPortAttachmentHolder.of(o));
    }

    public static PortAttachmentHolder of(Entity o) {
        return new PortAttachmentHolder(CPortAttachmentHolder.of(o));
    }

    public static PortAttachmentHolder of(Level o) {
        return new PortAttachmentHolder(CPortAttachmentHolder.of(o));
    }

    public static PortAttachmentHolder of(ChunkAccess o) {
        return new PortAttachmentHolder(CPortAttachmentHolder.of(o));
    }
}
