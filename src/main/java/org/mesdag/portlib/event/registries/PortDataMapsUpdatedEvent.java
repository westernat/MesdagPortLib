package org.mesdag.portlib.event.registries;

import PortLib.extensions.net.minecraftforge.registries.IForgeRegistry.PortIForgeRegistryExtension;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.registries.IForgeRegistry;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.core.PortRegistry;

import java.util.function.Consumer;

public class PortDataMapsUpdatedEvent extends Event {
    private final RegistryAccess registryAccess;
    private final PortRegistry<?> registry;
    private final PortUpdateCause cause;

    @Diff
    public PortDataMapsUpdatedEvent(RegistryAccess registryAccess, IForgeRegistry<?> registry, PortUpdateCause cause) {
        this.registryAccess = registryAccess;
        this.registry = PortIForgeRegistryExtension.wrap(registry);
        this.cause = cause;
    }

    public RegistryAccess getRegistries() {
        return registryAccess;
    }

    public PortRegistry<?> getRegistry() {
        return registry;
    }

    public ResourceKey<? extends Registry<?>> getRegistryKey() {
        return registry.key();
    }

    @SuppressWarnings("unchecked")
    public <T> void ifRegistry(ResourceKey<Registry<T>> type, Consumer<Registry<T>> consumer) {
        if (getRegistryKey() == type) {
            consumer.accept((Registry<T>) registry);
        }
    }

    public PortUpdateCause getCause() {
        return cause;
    }

    public enum PortUpdateCause {
        CLIENT_SYNC,
        SERVER_RELOAD
    }
}
