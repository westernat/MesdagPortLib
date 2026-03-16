package org.mesdag.portlib.attachment;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.neoforged.neoforge.attachment.AttachmentHolder;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class PortAttachmentHolder {
    private final AttachmentHolder delegate;

    private PortAttachmentHolder(AttachmentHolder delegate) {
        this.delegate = delegate;
    }

    public boolean hasAttachments() {
        return delegate.hasAttachments();
    }

    public boolean hasData(PortAttachmentType<?> type) {
        return delegate.hasData(type.unwrap());
    }

    public <T> boolean hasData(Supplier<PortAttachmentType<T>> type) {
        return delegate.hasData(type.get().unwrap());
    }

    public <T> T getData(PortAttachmentType<T> type) {
        return delegate.getData(type.unwrap());
    }

    public <T> T getData(Supplier<PortAttachmentType<T>> type) {
        return delegate.getData(type.get().unwrap());
    }

    public <T> @Nullable T getExistingDataOrNull(PortAttachmentType<T> type) {
        return delegate.getExistingDataOrNull(type.unwrap());
    }

    public <T> @Nullable T getExistingDataOrNull(Supplier<PortAttachmentType<T>> type) {
        return delegate.getExistingDataOrNull(type.get().unwrap());
    }

    public <T> @Nullable T setData(PortAttachmentType<T> type, T data) {
        return delegate.setData(type.unwrap(), data);
    }

    public <T> @Nullable T setData(Supplier<PortAttachmentType<T>> type, T data) {
        return delegate.setData(type.get().unwrap(), data);
    }

    public <T> @Nullable T removeData(PortAttachmentType<T> type) {
        return delegate.removeData(type.unwrap());
    }

    public <T> @Nullable T removeData(Supplier<PortAttachmentType<T>> type) {
        return delegate.removeData(type.get().unwrap());
    }

    public static PortAttachmentHolder of(BlockEntity o) {
        return new PortAttachmentHolder(o);
    }

    public static PortAttachmentHolder of(Entity o) {
        return new PortAttachmentHolder(o);
    }

    public static PortAttachmentHolder of(Level o) {
        return new PortAttachmentHolder(o);
    }

    public static PortAttachmentHolder of(ChunkAccess o) {
        return new PortAttachmentHolder(o.getAttachmentHolder());
    }
}
