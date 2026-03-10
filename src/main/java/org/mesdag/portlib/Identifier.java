package org.mesdag.portlib;

import net.minecraft.resources.ResourceLocation;

public class Identifier extends ResourceLocation {
    Identifier(String namespace, String path) {
        super(namespace, path);
    }

    Identifier(String location) {
        super(ResourceLocation.DEFAULT_NAMESPACE, location);
    }
}
