package org.mesdag.portlib.registries;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import net.minecraftforge.registries.DeferredRegister;
import org.jetbrains.annotations.ApiStatus;
import org.mesdag.portlib.attachment.PortAttachmentType;
import org.mesdag.portlib.diff.PortRegistries;
import org.mesdag.portlib.event.PortBus;
import org.mesdag.portlib.wrapper.resources.PortIdentifier;

@SuppressWarnings("unchecked")
public class PortAttachmentRegistration extends PortRegistration<PortAttachmentType<?>> {
    private final DeferredRegister<PortAttachmentType<?>> register;

    PortAttachmentRegistration(String namespace) {
        super(namespace, PortRegistries.Keys.ATTACHMENT_TYPES);
        this.register = DeferredRegister.create(PortRegistries.Keys.ATTACHMENT_TYPES, namespace);
        register.register(PortBus.MOD.unwrap());
    }

    @ApiStatus.Internal
    @Override
    public PortRegistryEntry<PortAttachmentType<?>> register(String name, Supplier<PortAttachmentType<?>> valueSupplier) {
        Supplier<PortAttachmentType<?>> memoize = Suppliers.memoize(valueSupplier);
        register.register(name, memoize);
        return new PortRegistryEntry<>(PortIdentifier.fromNamespaceAndPath(namespace, name), memoize) {
            @Override
            public PortAttachmentType<?> get() {
                return valueSupplier.get(); // == memoize.get()
            }
        };
    }

    public <T> PortRegistryEntry<PortAttachmentType<T>> registerTyped(String name, Supplier<PortAttachmentType<T>> valueSupplier) {
        return (PortRegistryEntry<PortAttachmentType<T>>) (PortRegistryEntry<?>) register(name, cast(valueSupplier));
    }

    private static <T> Supplier<PortAttachmentType<?>> cast(Supplier<T> supplier) {
        return (Supplier<PortAttachmentType<?>>) supplier;
    }
}
