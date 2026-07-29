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
        // 目标尺寸与源纹理尺寸必须分开传递。若使用参数较少的 1.20 blit 重载，
        // width/height 会同时充当源区域尺寸，控件缩放时只能截取纹理左上角。
        thiz.blit(sprite.path(), x, y, width, height, 0.0F, 0.0F, sprite.textureW(), sprite.textureH(), sprite.textureW(), sprite.textureH());
    }
}
