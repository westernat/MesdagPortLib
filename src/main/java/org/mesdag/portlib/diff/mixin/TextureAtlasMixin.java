package org.mesdag.portlib.diff.mixin;

import net.minecraft.client.renderer.texture.TextureAtlas;
import org.mesdag.portlib.wrapper.common.extensions.IPortTextureAtlasExtension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TextureAtlas.class)
public abstract class TextureAtlasMixin implements IPortTextureAtlasExtension {
}
