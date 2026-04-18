package org.mesdag.portlib.event.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.client.event.RenderHighlightEvent;
import org.mesdag.portlib.client.PortDeltaTicker;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public abstract class PortRenderHighlightEvent<E extends RenderHighlightEvent> extends PortEvent<E> {
    @Diff
    public PortRenderHighlightEvent(E e) {
        super(e);
    }

    public LevelRenderer getLevelRenderer() {
        return e.getLevelRenderer();
    }

    public Camera getCamera() {
        return e.getCamera();
    }

    public HitResult getTarget() {
        return e.getTarget();
    }

    public PortDeltaTicker getDeltaTracker() {
        return e.getDeltaTracker().wrap();
    }

    public PoseStack getPoseStack() {
        return e.getPoseStack();
    }

    public MultiBufferSource getMultiBufferSource() {
        return e.getMultiBufferSource();
    }

    public static class PortBlock extends PortRenderHighlightEvent<RenderHighlightEvent.Block> implements IPortCancellableEvent {
        @Diff
        public PortBlock(RenderHighlightEvent.Block e) {
            super(e);
        }

        @Override
        public BlockHitResult getTarget() {
            return e.getTarget();
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class PortEntity extends PortRenderHighlightEvent<RenderHighlightEvent.Entity> {
        @Diff
        public PortEntity(RenderHighlightEvent.Entity e) {
            super(e);
        }

        @Override
        public EntityHitResult getTarget() {
            return e.getTarget();
        }

        static {
            PortEventHooks.register();
        }
    }
}
