package org.mesdag.portlib.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import org.mesdag.portlib.diff.Diff;

public interface GuiLayer {
    void render(GuiGraphics guiGraphics, PortDeltaTicker ticker);

    @Diff
    default LayeredDraw.Layer unwrap() {
        return (guiGraphics, tracker) -> render(guiGraphics, tracker.wrap());
    }

    @Diff
    record Delegate(LayeredDraw.Layer delegate) implements GuiLayer {
        @Override
        public void render(GuiGraphics guiGraphics, PortDeltaTicker ticker) {
            delegate.render(guiGraphics, ticker.unwrap());
        }

        @Override
        public LayeredDraw.Layer unwrap() {
            return delegate;
        }
    }
}
