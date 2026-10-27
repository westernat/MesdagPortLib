package org.mesdag.portlib.wrapper.common.extensions;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import org.mesdag.portlib.client.gui.PortGuiMetadataSection;
import org.mesdag.portlib.client.gui.PortGuiSpriteScaling;
import org.mesdag.portlib.diff.PortModelManager;

@SuppressWarnings("all")
public interface IPortGuiGraphicsExtension {
    private GuiGraphics self() {
        return (GuiGraphics) this;
    }

    default void blitSprite(ResourceLocation sprite, int x, int y, int width, int height) {
        blitSprite(sprite, x, y, 0, width, height);
    }

    default void blitSprite(ResourceLocation sprite, int x, int y, int width, int height, int textureWidth, int textureHeight) {
        if (sprite.getPath().endsWith(".png")) {
            if (!sprite.getPath().startsWith("textures/gui/sprites/")) {
                self().blit(sprite, x, y, width, height, 0.0F, 0.0F, textureWidth, textureHeight, textureWidth, textureHeight);
                return;
            }

            sprite = sprite.withPath(path -> path.substring("textures/gui/sprites/".length(), path.length() - ".png".length()));
        }

        blitSprite(sprite, x, y, width, height);
    }

    default void blitSprite(ResourceLocation sprite, int x, int y, int blitOffset, int width, int height) {
        if (width == 0 || height == 0) {
            return;
        }

        TextureAtlasSprite atlasSprite = guiSprite(sprite);
        PortGuiSpriteScaling scaling = guiScaling(atlasSprite);
        if (scaling instanceof PortGuiSpriteScaling.Tile tile) {
            blitTiledSprite(atlasSprite, x, y, blitOffset, width, height, 0, 0, tile.width(), tile.height(), tile.width(), tile.height());
        } else if (scaling instanceof PortGuiSpriteScaling.NineSlice nineSlice) {
            blitNineSlicedSprite(atlasSprite, nineSlice, x, y, blitOffset, width, height);
        } else {
            self().blit(x, y, blitOffset, width, height, atlasSprite);
        }
    }

    default void blitSprite(ResourceLocation sprite, int textureWidth, int textureHeight, int uPosition, int vPosition, int x, int y, int uWidth, int vHeight) {
        if (sprite.getPath().endsWith(".png")) {
            if (!sprite.getPath().startsWith("textures/gui/sprites/")) {
                self().blit(sprite, x, y, uPosition, vPosition, uWidth, vHeight, textureWidth, textureHeight);
                return;
            }

            sprite = sprite.withPath(path -> path.substring("textures/gui/sprites/".length(), path.length() - ".png".length()));
        }

        blitSprite(sprite, textureWidth, textureHeight, uPosition, vPosition, x, y, 0, uWidth, vHeight);
    }

    default void blitSprite(ResourceLocation sprite, int textureWidth, int textureHeight, int uPosition, int vPosition, int x, int y, int blitOffset, int uWidth, int vHeight) {
        if (uWidth == 0 || vHeight == 0) {
            return;
        }

        TextureAtlasSprite atlasSprite = guiSprite(sprite);
        if (guiScaling(atlasSprite) instanceof PortGuiSpriteScaling.Stretch) {
            blitSprite(atlasSprite, textureWidth, textureHeight, uPosition, vPosition, x, y, blitOffset, uWidth, vHeight);
        } else {
            self().blit(x, y, blitOffset, uWidth, vHeight, atlasSprite);
        }
    }

    private TextureAtlasSprite guiSprite(ResourceLocation sprite) {
        return Minecraft.getInstance().getModelManager().getAtlas(PortModelManager.GUI_ATLAS).getSprite(sprite);
    }

    private PortGuiSpriteScaling guiScaling(TextureAtlasSprite sprite) {
        return IPortSpriteContentsExtension.of(sprite.contents()).metadata().getSection(PortGuiMetadataSection.TYPE)
                .orElse(PortGuiMetadataSection.DEFAULT).scaling();
    }

    private void blitSprite(TextureAtlasSprite sprite, int textureWidth, int textureHeight, int uPosition, int vPosition, int x, int y, int blitOffset, int width, int height) {
        if (width == 0 || height == 0) {
            return;
        }

        /// 1.20.1 的精灵 UV 使用 0 到 16 的坐标，裁剪范围相对于单帧而非整个图集。
        portlib$innerBlit(sprite.atlasLocation(), x, x + width, y, y + height, blitOffset,
                sprite.getU(16.0 * uPosition / textureWidth), sprite.getU(16.0 * (uPosition + width) / textureWidth),
                sprite.getV(16.0 * vPosition / textureHeight), sprite.getV(16.0 * (vPosition + height) / textureHeight));
    }

    default void blitTiledSprite(TextureAtlasSprite sprite, int x, int y, int blitOffset, int width, int height, int uPosition, int vPosition, int spriteWidth, int spriteHeight, int textureWidth, int textureHeight) {
        if (width <= 0 || height <= 0) {
            return;
        }

        if (spriteWidth <= 0 || spriteHeight <= 0) {
            throw new IllegalArgumentException("平铺纹理的尺寸必须为正数");
        }

        for (int offsetX = 0; offsetX < width; offsetX += spriteWidth) {
            int tileWidth = Math.min(spriteWidth, width - offsetX);
            for (int offsetY = 0; offsetY < height; offsetY += spriteHeight) {
                int tileHeight = Math.min(spriteHeight, height - offsetY);
                blitSprite(sprite, textureWidth, textureHeight, uPosition, vPosition, x + offsetX, y + offsetY, blitOffset, tileWidth, tileHeight);
            }
        }
    }

    private void blitNineSlicedSprite(TextureAtlasSprite sprite, PortGuiSpriteScaling.NineSlice scaling, int x, int y, int blitOffset, int width, int height) {
        int sourceWidth = scaling.width();
        int sourceHeight = scaling.height();
        PortGuiSpriteScaling.NineSlice.Border border = scaling.border();
        int left = Math.min(border.left(), width / 2);
        int right = Math.min(border.right(), width / 2);
        int top = Math.min(border.top(), height / 2);
        int bottom = Math.min(border.bottom(), height / 2);

        if (width == sourceWidth && height == sourceHeight) {
            blitSprite(sprite, sourceWidth, sourceHeight, 0, 0, x, y, blitOffset, width, height);
            return;
        }

        if (height == sourceHeight) {
            blitSprite(sprite, sourceWidth, sourceHeight, 0, 0, x, y, blitOffset, left, height);
            blitTiledSprite(sprite, x + left, y, blitOffset, width - left - right, height, left, 0, sourceWidth - left - right, sourceHeight, sourceWidth, sourceHeight);
            blitSprite(sprite, sourceWidth, sourceHeight, sourceWidth - right, 0, x + width - right, y, blitOffset, right, height);
            return;
        }

        if (width == sourceWidth) {
            blitSprite(sprite, sourceWidth, sourceHeight, 0, 0, x, y, blitOffset, width, top);
            blitTiledSprite(sprite, x, y + top, blitOffset, width, height - top - bottom, 0, top, sourceWidth, sourceHeight - top - bottom, sourceWidth, sourceHeight);
            blitSprite(sprite, sourceWidth, sourceHeight, 0, sourceHeight - bottom, x, y + height - bottom, blitOffset, width, bottom);
            return;
        }

        blitSprite(sprite, sourceWidth, sourceHeight, 0, 0, x, y, blitOffset, left, top);
        blitTiledSprite(sprite, x + left, y, blitOffset, width - left - right, top, left, 0, sourceWidth - left - right, top, sourceWidth, sourceHeight);
        blitSprite(sprite, sourceWidth, sourceHeight, sourceWidth - right, 0, x + width - right, y, blitOffset, right, top);
        blitSprite(sprite, sourceWidth, sourceHeight, 0, sourceHeight - bottom, x, y + height - bottom, blitOffset, left, bottom);
        blitTiledSprite(sprite, x + left, y + height - bottom, blitOffset, width - left - right, bottom, left, sourceHeight - bottom, sourceWidth - left - right, bottom, sourceWidth, sourceHeight);
        blitSprite(sprite, sourceWidth, sourceHeight, sourceWidth - right, sourceHeight - bottom, x + width - right, y + height - bottom, blitOffset, right, bottom);
        blitTiledSprite(sprite, x, y + top, blitOffset, left, height - top - bottom, 0, top, left, sourceHeight - top - bottom, sourceWidth, sourceHeight);
        blitTiledSprite(sprite, x + left, y + top, blitOffset, width - left - right, height - top - bottom, left, top, sourceWidth - left - right, sourceHeight - top - bottom, sourceWidth, sourceHeight);
        blitTiledSprite(sprite, x + width - right, y + top, blitOffset, right, height - top - bottom, sourceWidth - right, top, right, sourceHeight - top - bottom, sourceWidth, sourceHeight);
    }

    void portlib$innerBlit(ResourceLocation atlasLocation, int x1, int x2, int y1, int y2, int blitOffset, float minU, float maxU, float minV, float maxV);

    static IPortGuiGraphicsExtension of(GuiGraphics guiGraphics) {
        return (IPortGuiGraphicsExtension) guiGraphics;
    }
}
