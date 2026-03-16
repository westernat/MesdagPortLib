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
        super(namespace, PortRegistries.Keys.DATA_COMPONENTS);
        this.register = DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, namespace);
        register.register(PortBus.MOD.unwrap());
    }

    @ApiStatus.Internal
    @Override
    public PortRegistryEntry<PortDataComponentType<?>> register(String name, Supplier<PortDataComponentType<?>> valueSupplier) {
        Supplier<PortDataComponentType<?>> memoize = Suppliers.memoize(valueSupplier);
        register.register(name, () -> memoize.get().unwrap());
        return new PortRegistryEntry.Memoized<>(namespace, name, memoize);
    }

    public <T> PortRegistryEntry<PortDataComponentType<T>> registerTyped(String name, Consumer<PortDataComponentType.PortBuilder<T>> consumer) {
        return (PortRegistryEntry<PortDataComponentType<T>>) (PortRegistryEntry<?>) register(name, () -> {
            PortDataComponentType.PortBuilder<T> builder = new PortDataComponentType.PortBuilder<>();
            consumer.accept(builder);
            return builder.build();
        });
    }
}
