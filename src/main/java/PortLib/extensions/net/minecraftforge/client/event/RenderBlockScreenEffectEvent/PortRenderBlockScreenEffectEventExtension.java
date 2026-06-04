package PortLib.extensions.net.minecraftforge.client.event.RenderBlockScreenEffectEvent;

import net.minecraftforge.client.event.RenderBlockScreenEffectEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.client.PortRenderBlockScreenEffectEvent;

public class PortRenderBlockScreenEffectEventExtension {
    public static class OverlayType {
        @Diff
        public static PortRenderBlockScreenEffectEvent.PortOverlayType wrap(RenderBlockScreenEffectEvent.OverlayType thiz) {
            if (thiz == RenderBlockScreenEffectEvent.OverlayType.FIRE) {
                return PortRenderBlockScreenEffectEvent.PortOverlayType.FIRE;
            } else if (thiz == RenderBlockScreenEffectEvent.OverlayType.WATER) {
                return PortRenderBlockScreenEffectEvent.PortOverlayType.WATER;
            }
            return PortRenderBlockScreenEffectEvent.PortOverlayType.BLOCK;
        }
    }
}
