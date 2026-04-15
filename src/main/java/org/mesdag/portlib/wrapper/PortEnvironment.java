package org.mesdag.portlib.wrapper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.server.ServerLifecycleHooks;

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
        if (server == null) {
            if (isPhysicalClient()) {
                ClientPacketListener connection = Minecraft.getInstance().getConnection();
                if (connection != null) {
                    return connection.registryAccess();
                }
            }
        }
        return server == null ? RegistryAccess.EMPTY : server.registryAccess();
    }
}
