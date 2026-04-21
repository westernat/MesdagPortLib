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

public class PortAttachmentRegistration extends PortRegistration<PortAttachmentType<?>> {
    private final DeferredRegister<AttachmentType<?>> register;

    PortAttachmentRegistration(String namespace) {
        super(namespace, PortRegistries.Keys.ATTACHMENT_TYPES, false);
        this.register = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, namespace);
        register.register(PortBus.MOD.unwrap(namespace));
    }

    @ApiStatus.Internal
    @Override
    public <R extends PortAttachmentType<?>> PortRegistryEntry<PortAttachmentType<?>, R> register(String name, Supplier<R> valueSupplier) {
        Supplier<R> memoize = Suppliers.memoize(valueSupplier);
        register.register(name, () -> memoize.get().unwrap());
        PortRegistryEntry.Memoized<PortAttachmentType<?>, R> entry = new PortRegistryEntry.Memoized<>(namespace, name, memoize);
        entries.add(entry);
        return entry;
    }

    public <T> PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> registerSimple(String name, Supplier<PortAttachmentType.PortBuilder<T>> valueSupplier) {
        return register(name, () -> valueSupplier.get().build());
    }
}
