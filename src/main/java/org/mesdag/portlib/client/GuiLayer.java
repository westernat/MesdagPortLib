package org.mesdag.portlib.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import org.mesdag.portlib.diff.Diff;

public interface GuiLayer {
    void render(GuiGraphics guiGraphics, PortDeltaTicker ticker);

    @Diff
    default IGuiOverlay unwrap() {
        return (gui, guiGraphics, partialTick, screenWidth, screenHeight) ->
                render(guiGraphics, PortDeltaTicker.INSTANCE);
    }

    @Diff
    record Delegate(IGuiOverlay delegate) implements GuiLayer {
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
