package org.mesdag.portlib.registries;

import com.google.common.base.Supplier;
import org.jetbrains.annotations.ApiStatus;
import org.mesdag.portlib.component.PortDataComponentType;
import org.mesdag.portlib.diff.PortRegistries;

import java.util.function.Consumer;

public class PortDataComponentRegistration extends PortRegistration<PortDataComponentType<?>> {
    PortDataComponentRegistration(String namespace) {
        super(namespace, PortRegistries.Keys.DATA_COMPONENTS);
    }

    @ApiStatus.Internal
    @Override
    public <R extends PortDataComponentType<?>> PortRegistryEntry<PortDataComponentType<?>, R> register(String name, Supplier<R> valueSupplier) {
        return super.register(name, valueSupplier);
    }

    public <T> PortRegistryEntry<PortDataComponentType<?>, PortDataComponentType<T>> builder(String name, Consumer<PortDataComponentType.PortBuilder<T>> consumer) {
        return register(name, () -> {
            PortDataComponentType.PortBuilder<T> builder = new PortDataComponentType.PortBuilder<>();
            consumer.accept(builder);
            return builder.build();
        });
    }
}
