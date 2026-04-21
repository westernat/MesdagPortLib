package org.mesdag.portlib.registries;

import com.google.common.base.Supplier;
import org.jetbrains.annotations.ApiStatus;
import org.mesdag.portlib.attachment.PortAttachmentType;
import org.mesdag.portlib.diff.PortRegistries;

public class PortAttachmentRegistration extends PortRegistration<PortAttachmentType<?>> {
    PortAttachmentRegistration(String namespace) {
        super(namespace, PortRegistries.Keys.ATTACHMENT_TYPES);
    }

    @ApiStatus.Internal
    @Override
    public <R extends PortAttachmentType<?>> PortRegistryEntry<PortAttachmentType<?>, R> register(String name, Supplier<R> valueSupplier) {
        return super.register(name, valueSupplier);
    }

    public <T> PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> registerSimple(String name, Supplier<PortAttachmentType.PortBuilder<T>> valueSupplier) {
        return register(name, () -> valueSupplier.get().build());
    }
}
