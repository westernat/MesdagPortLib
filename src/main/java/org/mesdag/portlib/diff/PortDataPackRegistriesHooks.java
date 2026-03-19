package org.mesdag.portlib.diff;

import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySynchronization;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.mixin.DataPackRegistriesHooksAccessor;

public class PortDataPackRegistriesHooks {
    @ApiStatus.Internal
    @SuppressWarnings("unchecked")
    public static <T> @Nullable RegistrySynchronization.NetworkedRegistryData<T> getSyncedRegistry(final ResourceKey<? extends Registry<T>> registry) {
        return (RegistrySynchronization.NetworkedRegistryData<T>) DataPackRegistriesHooksAccessor.getNETWORKABLE_REGISTRIES().values().stream().filter(data -> data.key().equals(registry)).findFirst().orElse(null);
    }
}
