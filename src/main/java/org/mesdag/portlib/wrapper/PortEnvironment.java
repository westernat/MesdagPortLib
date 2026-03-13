package org.mesdag.portlib.wrapper;

import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.MinecraftServer;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.Objects;

@SuppressWarnings("all")
public class PortEnvironment {
    public static boolean isPhysicalClient() {
        return FMLEnvironment.dist.isClient();
    }

    public static boolean isPhysicalServer() {
        return FMLEnvironment.dist.isDedicatedServer();
    }

    public static boolean isDeveloper() {
        return !FMLEnvironment.production;
    }

    public static RegistryAccess registryAccess() {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null && isPhysicalClient()) {
            return Minecraft.getInstance().getConnection().registryAccess();
        }
        return Objects.requireNonNull(server, "No Server Found").registryAccess();
    }
}
