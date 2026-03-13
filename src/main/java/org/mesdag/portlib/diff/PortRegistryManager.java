package org.mesdag.portlib.diff;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegistryManager;

import java.util.Set;

@Diff
public class PortRegistryManager {
    private static Set<ResourceLocation> synced;

    public static boolean isNonSyncedBuiltInRegistry(Registry<?> registry) {
        if (synced == null) {
            synced = Set.copyOf(RegistryManager.getRegistryNamesForSyncToClient());
        }
        ForgeRegistry<?> registry1 = RegistryManager.ACTIVE.getRegistry(registry.key());
        if (registry1 == null) return false;
        return synced.contains(registry1.getRegistryName());
    }
}
