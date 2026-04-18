package PortLib.extensions.net.minecraft.client.gui.LayeredDraw;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.minecraft.client.gui.LayeredDraw;
import org.mesdag.portlib.client.GuiLayer;
import org.mesdag.portlib.diff.Diff;

@Extension
public class PortLayeredDrawExtension {
    public static class Layer {
        @Diff
        public static GuiLayer wrap(@This LayeredDraw.Layer thiz) {
            return new GuiLayer.Delegate(thiz);
        }
    }
}
