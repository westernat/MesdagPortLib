package org.mesdag.portlib.event.registries;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;
import net.neoforged.neoforge.registries.ModifyRegistriesEvent;
import org.apache.commons.lang3.stream.Streams;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.core.PortRegistry;

public class PortModifyRegistriesEvent extends Event implements IModBusEvent {
    private final ModifyRegistriesEvent delegate;

    @Diff
    public PortModifyRegistriesEvent(ModifyRegistriesEvent delegate) {
        this.delegate = delegate;
    }

    public Iterable<PortRegistry<?>> getRegistries() {
        return Streams.of(delegate.getRegistries()).<PortRegistry<?>>map(PortRegistry::wrap).toList();
    }

    public <T> PortRegistry<T> getRegistry(ResourceKey<? extends Registry<T>> key) {
        return PortRegistry.wrap(delegate.getRegistry(key));
    }
}
