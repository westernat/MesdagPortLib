package org.mesdag.portlib;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.mesdag.portlib.register.RegisterHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unused")
@Mod(PortLib.MODID)
public class PortLib {
    public static final String MODID = "portlib";
    public static final Logger LOGGER = LoggerFactory.getLogger("PortLib");

    public PortLib() {
        RegisterHandler.init();
    }

    public static Identifier identifier(String namespace, String path) {
        return new Identifier(namespace, path);
    }

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
