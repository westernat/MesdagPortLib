package org.mesdag.portlib.client.gui.components;

import PortLib.extensions.net.minecraft.client.gui.GuiGraphics.PortGuiGraphicsExtension;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class PortImageButton extends Button {
    private final PortWidgetSprites sprites;

    public PortImageButton(int x, int y, int width, int height, PortWidgetSprites sprites, OnPress onPress, Component message) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
        this.sprites = sprites;
    }

    public PortImageButton(int x, int y, int width, int height, PortWidgetSprites sprites, OnPress onPress) {
        this(x, y, width, height, sprites, onPress, CommonComponents.EMPTY);
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        PortSprite sprite = sprites.get(isActive(), isHoveredOrFocused());
        // 1.20 的普通 blit 重载默认按 256×256 解释 UV；必须经桥层传入真实源尺寸，
        // 再把完整纹理缩放到当前控件尺寸。
        PortGuiGraphicsExtension.blitSprite(
                guiGraphics, sprite, getX(), getY(), getWidth(), getHeight());
    }
}
