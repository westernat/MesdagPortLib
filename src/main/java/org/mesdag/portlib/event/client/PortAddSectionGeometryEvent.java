package org.mesdag.portlib.event.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.event.AddSectionGeometryEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.util.PortLists;
import org.mesdag.portlib.wrapper.client.model.lighting.PortQuadLighter;

import java.util.List;

public class PortAddSectionGeometryEvent extends PortEvent<AddSectionGeometryEvent> {
    @Diff
    public PortAddSectionGeometryEvent(AddSectionGeometryEvent e) {
        super(e);
    }

    public void addRenderer(PortAdditionalSectionRenderer renderer) {
        e.addRenderer(renderer.unwrap());
    }

    public List<PortAdditionalSectionRenderer> getAdditionalRenderers() {
        return PortLists.mutableTransform(e.getAdditionalRenderers(), AddSectionGeometryEvent.AdditionalSectionRenderer::wrap, PortAdditionalSectionRenderer::unwrap);
    }

    public BlockPos getSectionOrigin() {
        return e.getSectionOrigin();
    }

    public Level getLevel() {
        return e.getLevel();
    }

    static {
        PortEventHooks.register();
    }

    public interface PortAdditionalSectionRenderer {
        void render(PortSectionRenderingContext context);

        @Diff
        default AddSectionGeometryEvent.AdditionalSectionRenderer unwrap() {
            return context -> render(context.wrap());
        }

        @Diff
        record Delegate(AddSectionGeometryEvent.AdditionalSectionRenderer delegate) implements PortAdditionalSectionRenderer {
            @Override
            public void render(PortSectionRenderingContext context) {
                delegate.render(context.delegate);
            }

            @Override
            public AddSectionGeometryEvent.AdditionalSectionRenderer unwrap() {
                return delegate;
            }
        }
    }

    public static final class PortSectionRenderingContext {
        private final AddSectionGeometryEvent.SectionRenderingContext delegate;

        @Diff
        public PortSectionRenderingContext(AddSectionGeometryEvent.SectionRenderingContext delegate) {
            this.delegate = delegate;
        }

        @Diff
        public AddSectionGeometryEvent.SectionRenderingContext unwrap() {
            return delegate;
        }

        public VertexConsumer getOrCreateChunkBuffer(RenderType type) {
            return delegate.getOrCreateChunkBuffer(type);
        }

        public PortQuadLighter getQuadLighter(boolean smooth) {
            return delegate.getQuadLighter(smooth).wrap();
        }

        public PoseStack getPoseStack() {
            return delegate.getPoseStack();
        }

        public BlockAndTintGetter getRegion() {
            return delegate.getRegion();
        }
    }
}
