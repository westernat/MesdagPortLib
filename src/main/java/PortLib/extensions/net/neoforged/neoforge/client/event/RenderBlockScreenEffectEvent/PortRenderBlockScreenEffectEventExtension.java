package PortLib.extensions.net.neoforged.neoforge.client.event.RenderBlockScreenEffectEvent;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.neoforged.neoforge.client.event.RenderBlockScreenEffectEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.client.PortRenderBlockScreenEffectEvent;

@Extension
public class PortRenderBlockScreenEffectEventExtension {
    public static class OverlayType {
        @Diff
        public static PortRenderBlockScreenEffectEvent.PortOverlayType wrap(@This RenderBlockScreenEffectEvent.OverlayType thiz) {
            if (thiz == RenderBlockScreenEffectEvent.OverlayType.FIRE) {
                return PortRenderBlockScreenEffectEvent.PortOverlayType.FIRE;
            } else if (thiz == RenderBlockScreenEffectEvent.OverlayType.WATER) {
                return PortRenderBlockScreenEffectEvent.PortOverlayType.WATER;
            }
            return PortRenderBlockScreenEffectEvent.PortOverlayType.BLOCK;
        }
    }
}
