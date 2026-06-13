package PortLib.extensions.net.minecraft.client.gui.GuiGraphics;

import net.minecraft.client.gui.GuiGraphics;
import org.mesdag.portlib.client.gui.components.PortSprite;

public class PortGuiGraphicsExtension {
    public static void blitSprite(
            GuiGraphics thiz,
            PortSprite sprite,
            int textureWidth,
            int textureHeight,
            int uPosition,
            int vPosition,
            int x,
            int y,
            int uWidth,
            int vHeight
    ) {
        thiz.blit(sprite.path(), x, y, uPosition, vPosition, uWidth, vHeight, textureWidth, textureHeight);
    }

    public static void blitSprite(
            GuiGraphics thiz,
            PortSprite sprite,
            int x,
            int y,
            int width,
            int height
    ) {
        thiz.blit(sprite.path(), x, y, 0, 0, width, height, sprite.textureW(), sprite.textureH());
    }
}
