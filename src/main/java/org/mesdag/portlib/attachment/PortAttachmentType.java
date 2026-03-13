package org.mesdag.portlib.attachment;

import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.util.Var;
import org.mesdag.portlib.wrapper.IPortNBTSerializable;

import java.util.function.Function;
import java.util.function.Supplier;

public class PortAttachmentType<T> {
    private final AttachmentType<T> delegate;

    private PortAttachmentType(AttachmentType<T> delegate) {
        this.delegate = delegate;
    }

    @Var
    public static <T> AttachmentType.Builder<T> builder(Supplier<T> defaultValueSupplier) {
        return AttachmentType.builder(defaultValueSupplier);
    }

    @Var
    public static <T> AttachmentType.Builder<T> builder(Function<IAttachmentHolder, T> defaultValueConstructor) {
        return AttachmentType.builder(defaultValueConstructor);
    }

    @Var
    public static <S extends Tag, T extends IPortNBTSerializable<S>> AttachmentType.Builder<T> serializable(Supplier<T> defaultValueSupplier) {
        return AttachmentType.serializable(defaultValueSupplier);
    }

    @Var
    public static <S extends Tag, T extends IPortNBTSerializable<S>> AttachmentType.Builder<T> serializable(Function<IAttachmentHolder, T> defaultValueConstructor) {
        return AttachmentType.serializable(defaultValueConstructor);
    }

    @Diff
    public AttachmentType<T> unwrap() {
        return delegate;
    }

    @Diff
    public static <T> PortAttachmentType<T> wrap(AttachmentType<T> delegate) {
        return new PortAttachmentType<>(delegate);
    }
}
