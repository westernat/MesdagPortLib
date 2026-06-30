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
    public <T extends PortAttachmentType<?>> PortRegistryEntry<PortAttachmentType<?>, T> register(String name, Supplier<T> valueSupplier) {
        return super.register(name, valueSupplier);
    }

    public <T> PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> registerSimple(String name, Supplier<PortAttachmentType.Builder<T>> valueSupplier) {
        return register(name, () -> valueSupplier.get().build());
    }
}
