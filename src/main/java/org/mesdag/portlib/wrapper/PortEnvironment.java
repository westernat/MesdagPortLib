package org.mesdag.portlib.wrapper;

import net.neoforged.fml.loading.FMLEnvironment;

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
}
