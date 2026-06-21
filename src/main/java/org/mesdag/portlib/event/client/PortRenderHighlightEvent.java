package org.mesdag.portlib.event.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.event.RenderHighlightEvent;
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
        return PortDeltaTicker.INSTANCE;
    }

    public PoseStack getPoseStack() {
        return e.getPoseStack();
    }

    public MultiBufferSource getMultiBufferSource() {
        return e.getMultiBufferSource();
    }

    public static class Block extends PortRenderHighlightEvent<RenderHighlightEvent.Block> implements IPortCancellableEvent {
        @Diff
        public Block(RenderHighlightEvent.Block e) {
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

    public static class Entity extends PortRenderHighlightEvent<RenderHighlightEvent.Entity> {
        @Diff
        public Entity(RenderHighlightEvent.Entity e) {
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
