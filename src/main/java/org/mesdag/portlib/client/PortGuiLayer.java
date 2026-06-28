package org.mesdag.portlib.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import org.mesdag.portlib.diff.Diff;

/// to replace `LayeredDraw.Layer`
public interface PortGuiLayer {
    void render(GuiGraphics guiGraphics, PortDeltaTicker ticker);

    @Diff
    default IGuiOverlay unwrap() {
        return (gui, guiGraphics, partialTick, screenWidth, screenHeight) ->
                render(guiGraphics, PortDeltaTicker.INSTANCE);
    }

    @Diff
    record Delegate(IGuiOverlay delegate) implements PortGuiLayer {
        @Override
        public void render(GuiGraphics guiGraphics, PortDeltaTicker ticker) {
            delegate.render(
                    (ForgeGui) Minecraft.getInstance().gui,
                    guiGraphics,
                    ticker.unwrap().partialTick,
                    guiGraphics.guiWidth(),
                    guiGraphics.guiHeight()
            );
        }

        @Override
        public IGuiOverlay unwrap() {
            return delegate;
        }
    }
}
