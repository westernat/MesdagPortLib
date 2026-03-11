package org.mesdag.portlib.wrapper;

import net.neoforged.fml.loading.FMLPaths;

import java.nio.file.Path;

public class PortPaths {
    public static Path gamedir() {
        return FMLPaths.GAMEDIR.get();
    }

    public static Path modsdir() {
        return FMLPaths.MODSDIR.get();
    }

    public static Path configdir() {
        return FMLPaths.CONFIGDIR.get();
    }

    public static Path fmlconfig() {
        return FMLPaths.FMLCONFIG.get();
    }
}
