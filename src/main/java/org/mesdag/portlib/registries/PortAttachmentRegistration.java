package org.mesdag.portlib.registries;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.ApiStatus;
import org.mesdag.portlib.attachment.PortAttachmentType;
import org.mesdag.portlib.diff.PortRegistries;
import org.mesdag.portlib.event.PortBus;

@SuppressWarnings("unchecked")
public class PortAttachmentRegistration extends PortRegistration<PortAttachmentType<?>> {
    private final DeferredRegister<AttachmentType<?>> register;

    PortAttachmentRegistration(String namespace) {
        super(namespace, PortRegistries.Keys.ATTACHMENT_TYPES);
        this.register = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, namespace);
        register.register(PortBus.MOD.unwrap(namespace));
    }

    @ApiStatus.Internal
    @Override
    public PortRegistryEntry<PortAttachmentType<?>> register(String name, Supplier<PortAttachmentType<?>> valueSupplier) {
        Supplier<PortAttachmentType<?>> memoize = Suppliers.memoize(valueSupplier);
        register.register(name, () -> memoize.get().unwrap());
        return new PortRegistryEntry.Memoized<>(namespace, name, memoize);
    }

    public <T> PortRegistryEntry<PortAttachmentType<T>> registerTyped(String name, Supplier<PortAttachmentType.PortBuilder<T>> valueSupplier) {
        return (PortRegistryEntry<PortAttachmentType<T>>) (PortRegistryEntry<?>) register(name, () -> valueSupplier.get().build());
    }
}
