package org.mesdag.portlib.event.registries.datamaps;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;
import net.minecraftforge.registries.DataPackRegistriesHooks;
import org.jetbrains.annotations.ApiStatus;
import org.mesdag.portlib.datamap.DataMapType;
import org.mesdag.portlib.diff.PortDataPackRegistriesHooks;

import java.util.HashMap;
import java.util.Map;

public class PortRegisterDataMapTypesEvent extends Event implements IModBusEvent {
    private final Map<ResourceKey<Registry<?>>, Map<ResourceLocation, DataMapType<?, ?>>> attachments;

    @ApiStatus.Internal
    public PortRegisterDataMapTypesEvent(Map<ResourceKey<Registry<?>>, Map<ResourceLocation, DataMapType<?, ?>>> attachments) {
        this.attachments = attachments;
    }

    public <T, R> void register(DataMapType<R, T> type) {
        final var registry = type.registryKey();
        if (DataPackRegistriesHooks.getDataPackRegistries().stream().anyMatch(data -> data.key().equals(registry))) {
            if (type.networkCodec() != null && PortDataPackRegistriesHooks.getSyncedRegistry(registry) == null) {
                throw new UnsupportedOperationException("Cannot register synced data map " + type.id() + " for datapack registry " + registry.location() + " that is not synced!");
            }
        }

        final var map = attachments.computeIfAbsent((ResourceKey) registry, k -> new HashMap<>());
        if (map.containsKey(type.id())) {
            throw new IllegalArgumentException("Tried to register data map type with ID " + type.id() + " to registry " + registry.location() + " twice");
        }
        map.put(type.id(), type);
    }
}
