package org.mesdag.portlib.registries;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import net.minecraftforge.registries.DeferredRegister;
import org.jetbrains.annotations.ApiStatus;
import org.mesdag.portlib.attachment.PortAttachmentType;
import org.mesdag.portlib.diff.PortRegistries;
import org.mesdag.portlib.event.PortBus;

@SuppressWarnings("unchecked")
public class PortAttachmentRegistration extends PortRegistration<PortAttachmentType<?>> {
    private final DeferredRegister<PortAttachmentType<?>> register;

    PortAttachmentRegistration(String namespace) {
        super(namespace, PortRegistries.Keys.ATTACHMENT_TYPES);
        this.register = DeferredRegister.create(PortRegistries.Keys.ATTACHMENT_TYPES, namespace);
        register.register(PortBus.MOD.unwrap(namespace));
    }

    @ApiStatus.Internal
    @Override
    public PortRegistryEntry<PortAttachmentType<?>> register(String name, Supplier<PortAttachmentType<?>> valueSupplier) {
        Supplier<PortAttachmentType<?>> memoize = Suppliers.memoize(valueSupplier);
        register.register(name, memoize);
        return new PortRegistryEntry.Memoized<>(namespace, name, memoize);
    }

    public <T> PortRegistryEntry<PortAttachmentType<T>> registerTyped(String name, Supplier<PortAttachmentType.PortBuilder<T>> valueSupplier) {
        return (PortRegistryEntry<PortAttachmentType<T>>) (PortRegistryEntry<?>) register(name, () -> cast(valueSupplier).get().build());
    }

    private static <T> Supplier<PortAttachmentType.PortBuilder<?>> cast(Supplier<T> supplier) {
        return (Supplier<PortAttachmentType.PortBuilder<?>>) supplier;
    }
}
