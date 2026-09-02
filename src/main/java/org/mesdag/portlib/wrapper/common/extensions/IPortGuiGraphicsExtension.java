package org.mesdag.portlib.wrapper.common.extensions;

import net.minecraft.client.gui.GuiGraphics;
import org.mesdag.portlib.client.gui.components.PortSprite;

@SuppressWarnings("all")
public interface IPortGuiGraphicsExtension {
    private GuiGraphics self() {
        return (GuiGraphics) this;
    }

    default void blitSprite(PortSprite sprite, int textureWidth, int textureHeight, int uPosition, int vPosition, int x, int y, int uWidth, int vHeight) {
        self().blit(sprite.path(), x, y, uPosition, vPosition, uWidth, vHeight, textureWidth, textureHeight);
    }

    default void blitSprite(PortSprite sprite, int x, int y, int width, int height) {
        self().blit(sprite.path(), x, y, width, height, 0.0F, 0.0F, sprite.textureW(), sprite.textureH(), sprite.textureW(), sprite.textureH());
    }

    static IPortGuiGraphicsExtension of(GuiGraphics guiGraphics) {
        return (IPortGuiGraphicsExtension) guiGraphics;
    }
}
