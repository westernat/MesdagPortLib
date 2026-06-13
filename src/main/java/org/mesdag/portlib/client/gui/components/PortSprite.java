package org.mesdag.portlib.client.gui.components;

import net.minecraft.resources.ResourceLocation;

public record PortSprite(ResourceLocation path, int textureW, int textureH) {
    public PortSprite {
        if (!path.getPath().endsWith(".png")) {
            path = ResourceLocation.fromNamespaceAndPath(path.getNamespace(), "textures/gui/sprites/" + path.getPath() + ".png");
        }
    }
}
