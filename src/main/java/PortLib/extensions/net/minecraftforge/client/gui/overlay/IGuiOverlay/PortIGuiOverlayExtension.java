package PortLib.extensions.net.minecraftforge.client.gui.overlay.IGuiOverlay;

import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import org.mesdag.portlib.client.GuiLayer;
import org.mesdag.portlib.diff.Diff;

public class PortIGuiOverlayExtension {
    @Diff
    public static GuiLayer wrap(IGuiOverlay thiz) {
        return new GuiLayer.Delegate(thiz);
    }
}
