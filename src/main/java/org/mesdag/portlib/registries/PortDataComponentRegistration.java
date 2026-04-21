package org.mesdag.portlib.registries;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.ApiStatus;
import org.mesdag.portlib.component.PortDataComponentType;
import org.mesdag.portlib.diff.PortRegistries;
import org.mesdag.portlib.event.PortBus;

import java.util.function.Consumer;

public class PortDataComponentRegistration extends PortRegistration<PortDataComponentType<?>> {
    private final DeferredRegister<DataComponentType<?>> register;

    PortDataComponentRegistration(String namespace) {
        super(namespace, PortRegistries.Keys.DATA_COMPONENTS, false);
        this.register = DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, namespace);
        register.register(PortBus.MOD.unwrap(namespace));
    }

    @ApiStatus.Internal
    @Override
    public <R extends PortDataComponentType<?>> PortRegistryEntry<PortDataComponentType<?>, R> register(String name, Supplier<R> valueSupplier) {
        Supplier<R> memoize = Suppliers.memoize(valueSupplier);
        register.register(name, () -> memoize.get().unwrap());
        PortRegistryEntry.Memoized<PortDataComponentType<?>, R> entry = new PortRegistryEntry.Memoized<>(namespace, name, memoize);
        entries.add(entry);
        return entry;
    }

    public <T> PortRegistryEntry<PortDataComponentType<?>, PortDataComponentType<T>> register(String name, Consumer<PortDataComponentType.PortBuilder<T>> consumer) {
        return register(name, () -> {
            PortDataComponentType.PortBuilder<T> builder = new PortDataComponentType.PortBuilder<>();
            consumer.accept(builder);
            return builder.build();
        });
    }
}
