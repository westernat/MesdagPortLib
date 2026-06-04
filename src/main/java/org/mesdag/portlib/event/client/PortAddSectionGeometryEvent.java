package org.mesdag.portlib.event.client;

import PortLib.extensions.net.minecraftforge.client.model.lighting.QuadLighter.PortQuadLighterExtension;
import com.google.common.base.Preconditions;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.Event;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.diff.mixin.ForgeModelBlockRendererAccessor;
import org.mesdag.portlib.wrapper.client.model.lighting.PortQuadLighter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class PortAddSectionGeometryEvent extends Event {
    private List<PortAdditionalSectionRenderer> additionalRenderers;
    private final BlockPos sectionOrigin;
    private final Level level;

    @Diff
    public PortAddSectionGeometryEvent(BlockPos sectionOrigin, Level level) {
        this.sectionOrigin = sectionOrigin;
        this.level = level;
    }

    public void addRenderer(PortAdditionalSectionRenderer renderer) {
        getAdditionalRenderers().add(renderer);
    }

    public List<PortAdditionalSectionRenderer> getAdditionalRenderers() {
        if (additionalRenderers == null) {
            this.additionalRenderers = new ArrayList<>();
        }
        return additionalRenderers;
    }

    public BlockPos getSectionOrigin() {
        return sectionOrigin;
    }

    public Level getLevel() {
        Preconditions.checkState(Minecraft.getInstance().isSameThread());
        return level;
    }

    public interface PortAdditionalSectionRenderer {
        void render(PortSectionRenderingContext context);
    }

    public static final class PortSectionRenderingContext {
        private final Function<RenderType, VertexConsumer> getOrCreateLayer;
        private final BlockAndTintGetter region;
        private final PoseStack poseStack;

        public PortSectionRenderingContext(
                Function<RenderType, VertexConsumer> getOrCreateLayer,
                BlockAndTintGetter region,
                PoseStack poseStack
        ) {
            this.getOrCreateLayer = getOrCreateLayer;
            this.region = region;
            this.poseStack = poseStack;
        }

        public VertexConsumer getOrCreateChunkBuffer(RenderType type) {
            Preconditions.checkArgument(type.getChunkLayerId() != -1, "Cannot create a chunk render buffer for a non-chunk render type");
            return getOrCreateLayer.apply(type);
        }

        public PortQuadLighter getQuadLighter(boolean smooth) {
            var renderer = (ForgeModelBlockRendererAccessor) Minecraft.getInstance().getBlockRenderer().getModelRenderer();
            return PortQuadLighterExtension.wrap((smooth ? renderer.getSmoothLighter() : renderer.getFlatLighter()).get());
        }

        public PoseStack getPoseStack() {
            return poseStack;
        }

        public BlockAndTintGetter getRegion() {
            return region;
        }
    }
}
