package org.mesdag.portlib;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.mesdag.portlib.diff.PortRegistries;
import org.mesdag.portlib.registries.PortRegisterHandler;
import org.mesdag.portlib.wrapper.PortIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("all")
@Mod(PortLib.MODID)
public class PortLib {
    public static final String MODID = "portlib";
    public static final Logger LOGGER = LoggerFactory.getLogger("PortLib");

    public PortLib() {
        PortRegisterHandler.init();
        PortRegistries.init(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static PortIdentifier asResource(String path) {

        return PortIdentifier.fromNamespaceAndPath(MODID, path);
    }
}
