package org.mesdag.portlib.event.registries;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortModBusEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.registries.PortRegistryMaker;
import org.mesdag.portlib.wrapper.core.PortRegistry;

import java.util.function.Consumer;

public class PortNewRegistryEvent extends PortEvent<NewRegistryEvent> implements IPortModBusEvent {
    @Diff
    public PortNewRegistryEvent(NewRegistryEvent e) {
        super(e);
    }

    public <T> PortRegistry<T> create(ResourceKey<? extends Registry<T>> registryKey, Consumer<PortRegistryMaker<T>> consumer) {
        RegistryBuilder<T> builder = new RegistryBuilder<>(registryKey);
        PortRegistryMaker<T> maker = new PortRegistryMaker<>();
        maker.builder = builder;
        consumer.accept(maker);
        Registry<T> registry = builder.create();
        e.register(registry);
        return PortRegistry.wrap(registry);
    }

    static {
        PortEventHooks.register(NewRegistryEvent.class, PortNewRegistryEvent.class, PortNewRegistryEvent::new);
    }
}
