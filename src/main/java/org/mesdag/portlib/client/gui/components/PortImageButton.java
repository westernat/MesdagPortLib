package org.mesdag.portlib.client.gui.components;

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
        guiGraphics.blit(sprite.path(), getX(), getY(), 0, 0, sprite.width(), sprite.height());
    }
}
