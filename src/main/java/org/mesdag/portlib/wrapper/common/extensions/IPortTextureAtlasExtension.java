package org.mesdag.portlib.wrapper.common.extensions;

import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public interface IPortTextureAtlasExtension {
    default Map<ResourceLocation, TextureAtlasSprite> getTextures() {
        return ((TextureAtlas) this).texturesByName;
    }
}
