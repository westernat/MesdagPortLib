package org.mesdag.portlib.event.registries;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.ModifyRegistriesEvent;
import org.apache.commons.lang3.stream.Streams;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortModBusEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.wrapper.core.PortRegistry;

public class PortModifyRegistriesEvent extends PortEvent implements IPortModBusEvent {
    private final ModifyRegistriesEvent e;

    @Diff
    public PortModifyRegistriesEvent(ModifyRegistriesEvent e) {
        this.e = e;
    }

    public Iterable<PortRegistry<?>> getRegistries() {
        return Streams.of(e.getRegistries()).<PortRegistry<?>>map(PortRegistry::wrap).toList();
    }

    public <T> PortRegistry<T> getRegistry(ResourceKey<? extends Registry<T>> key) {
        return PortRegistry.wrap(e.getRegistry(key));
    }

    static {
        PortEventHooks.register(ModifyRegistriesEvent.class, PortModifyRegistriesEvent.class, PortModifyRegistriesEvent::new);
    }
}
