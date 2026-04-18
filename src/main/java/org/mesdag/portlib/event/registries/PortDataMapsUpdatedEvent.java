package org.mesdag.portlib.event.registries;

import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.datamaps.DataMapsUpdatedEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.wrapper.core.PortRegistry;

import java.util.function.Consumer;

public class PortDataMapsUpdatedEvent extends PortEvent<DataMapsUpdatedEvent> {
    @Diff
    public PortDataMapsUpdatedEvent(DataMapsUpdatedEvent e) {
        super(e);
    }

    public RegistryAccess getRegistries() {
        return e.getRegistries();
    }

    public PortRegistry<?> getRegistry() {
        return e.getRegistry().wrap();
    }

    public ResourceKey<? extends Registry<?>> getRegistryKey() {
        return e.getRegistryKey();
    }

    public <T> void ifRegistry(ResourceKey<Registry<T>> type, Consumer<Registry<T>> consumer) {
        e.ifRegistry(type, consumer);
    }

    public PortUpdateCause getCause() {
        return e.getCause().wrap();
    }

    public enum PortUpdateCause {
        CLIENT_SYNC,
        SERVER_RELOAD;

        @Diff
        public DataMapsUpdatedEvent.UpdateCause unwrap() {
            return this == CLIENT_SYNC ? DataMapsUpdatedEvent.UpdateCause.CLIENT_SYNC : DataMapsUpdatedEvent.UpdateCause.SERVER_RELOAD;
        }
    }

    static {
        PortEventHooks.register();
    }
}
