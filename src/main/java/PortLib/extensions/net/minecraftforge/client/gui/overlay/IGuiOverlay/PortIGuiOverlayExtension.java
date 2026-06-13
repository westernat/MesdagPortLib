package PortLib.extensions.net.minecraftforge.client.gui.overlay.IGuiOverlay;

import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import org.mesdag.portlib.client.PortGuiLayer;
import org.mesdag.portlib.diff.Diff;

public class PortIGuiOverlayExtension {
    @Diff
    public static PortGuiLayer wrap(IGuiOverlay thiz) {
        return new PortGuiLayer.Delegate(thiz);
    }
}
