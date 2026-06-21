package org.mesdag.portlib.wrapper.common.extensions;

import PortLib.extensions.net.minecraft.client.gui.GuiGraphics.PortGuiGraphicsExtension;
import net.minecraft.client.gui.GuiGraphics;
import org.mesdag.portlib.client.gui.components.PortSprite;

@SuppressWarnings("all")
public interface IPortGuiGraphicsExtension {

    private GuiGraphics self() {
        return (GuiGraphics) this;
    }

    default void blitSprite(PortSprite sprite, int textureWidth, int textureHeight, int uPosition, int vPosition, int x, int y, int uWidth, int vHeight) {
        PortGuiGraphicsExtension.blitSprite(self(), sprite, textureWidth, textureHeight, uPosition, vPosition, x, y, uWidth, vHeight);
    }

    default void blitSprite(PortSprite sprite, int x, int y, int width, int height) {
        PortGuiGraphicsExtension.blitSprite(self(), sprite, x, y, width, height);
    }

    static IPortGuiGraphicsExtension of(GuiGraphics guiGraphics) {
        return (IPortGuiGraphicsExtension) guiGraphics;
    }
}
