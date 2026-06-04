package PortLib.extensions.net.minecraftforge.client.event.RenderLevelStageEvent;

import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.client.PortRenderLevelStageEvent;

import java.util.Map;

public class PortRenderLevelStageEventExtension {
    public static class Stage {
        private static final Map<RenderLevelStageEvent.Stage, PortRenderLevelStageEvent.PortStage> WRAPPER = new Reference2ObjectOpenHashMap<>();

        @Diff
        public static PortRenderLevelStageEvent.PortStage wrap(RenderLevelStageEvent.Stage thiz) {
            return WRAPPER.computeIfAbsent(thiz, PortRenderLevelStageEvent.PortStage::new);
        }
    }
}
