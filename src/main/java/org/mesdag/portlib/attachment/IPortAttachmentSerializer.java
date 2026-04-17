package org.mesdag.portlib.attachment;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;

public interface IPortAttachmentSerializer<S extends Tag, T> {
    T read(IPortAttachmentHolder holder, S tag, HolderLookup.Provider provider);

    @Nullable S write(T attachment, HolderLookup.Provider provider);

    @Diff
    default IAttachmentSerializer<S, T> unwrap() {
        return new IAttachmentSerializer<>() {
            @Override
            public T read(IAttachmentHolder holder, S tag, HolderLookup.Provider provider) {
                return IPortAttachmentSerializer.this.read(IPortAttachmentHolder.wrap(holder), tag, provider);
            }

            @Override
            public @Nullable S write(T attachment, HolderLookup.Provider provider) {
                return IPortAttachmentSerializer.this.write(attachment, provider);
            }
        };
    }

    @Diff
    static <S extends Tag, T> IPortAttachmentSerializer<S, T> wrap(IAttachmentSerializer<S, T> delegate) {
        return new Delegate<>(delegate);
    }

    @Diff
    record Delegate<S extends Tag, T>(IAttachmentSerializer<S, T> delegate) implements IPortAttachmentSerializer<S, T> {
        @Override
        public T read(IPortAttachmentHolder holder, S tag, HolderLookup.Provider provider) {
            return null;
        }

        @Override
        public @Nullable S write(T attachment, HolderLookup.Provider provider) {
            return null;
        }

        @Override
        public IAttachmentSerializer<S, T> unwrap() {
            return delegate;
        }
    }
}
