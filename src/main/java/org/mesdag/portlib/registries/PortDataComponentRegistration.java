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
    public <T extends PortDataComponentType<?>> PortRegistryEntry<PortDataComponentType<?>, T> register(String name, Supplier<T> valueSupplier) {
        return super.register(name, valueSupplier);
    }

    public <T> PortRegistryEntry<PortDataComponentType<?>, PortDataComponentType<T>> builder(String name, Consumer<PortDataComponentType.Builder<T>> consumer) {
        return register(name, () -> {
            PortDataComponentType.Builder<T> builder = new PortDataComponentType.Builder<>();
            consumer.accept(builder);
            return builder.build();
        });
    }
}
