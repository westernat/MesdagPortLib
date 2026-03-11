package org.mesdag.portlib;

import net.minecraftforge.fml.common.Mod;
import org.mesdag.portlib.register.PortRegisterHandler;
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
    }

    public static PortIdentifier asResource(String path) {
        return PortIdentifier.fromNamespaceAndPath(MODID, path);
    }
}
