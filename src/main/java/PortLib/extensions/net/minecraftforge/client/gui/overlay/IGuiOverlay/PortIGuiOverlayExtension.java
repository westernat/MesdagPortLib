package PortLib.extensions.net.minecraftforge.client.gui.overlay.IGuiOverlay;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import org.mesdag.portlib.client.GuiLayer;
import org.mesdag.portlib.diff.Diff;

@Extension
public class PortIGuiOverlayExtension {
    @Diff
    public static GuiLayer wrap(@This IGuiOverlay thiz) {
        return new GuiLayer.Delegate(thiz);
    }
}
