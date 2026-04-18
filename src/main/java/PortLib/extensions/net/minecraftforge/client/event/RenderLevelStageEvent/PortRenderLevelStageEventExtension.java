package PortLib.extensions.net.minecraftforge.client.event.RenderLevelStageEvent;

import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.client.PortRenderLevelStageEvent;

import java.util.Map;

@Extension
public class PortRenderLevelStageEventExtension {
    public static class Stage {
        private static final Map<RenderLevelStageEvent.Stage, PortRenderLevelStageEvent.PortStage> WRAPPER = new Reference2ObjectOpenHashMap<>();

        @Diff
        public static PortRenderLevelStageEvent.PortStage wrap(@This RenderLevelStageEvent.Stage thiz) {
            return WRAPPER.computeIfAbsent(thiz, PortRenderLevelStageEvent.PortStage::new);
        }
    }
}
