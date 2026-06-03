package org.mesdag.portlib.event.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.mesdag.portlib.client.PortDeltaTicker;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortModBusEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortRenderLevelStageEvent extends PortEvent<RenderLevelStageEvent> {
    @Diff
    public PortRenderLevelStageEvent(RenderLevelStageEvent e) {
        super(e);
    }

    public PortStage getStage() {
        return e.getStage().wrap();
    }

    public LevelRenderer getLevelRenderer() {
        return e.getLevelRenderer();
    }

    public PoseStack getPoseStack() {
        return e.getPoseStack();
    }

    public Matrix4f getModelViewMatrix() {
        return e.getPoseStack().last().pose();
    }

    public Matrix4f getProjectionMatrix() {
        return e.getProjectionMatrix();
    }

    public int getRenderTick() {
        return e.getRenderTick();
    }

    public PortDeltaTicker getPartialTick() {
        return PortDeltaTicker.INSTANCE;
    }

    public Camera getCamera() {
        return e.getCamera();
    }

    public Frustum getFrustum() {
        return e.getFrustum();
    }

    static {
        PortEventHooks.register();
    }

    public static class PortRegisterStageEvent extends PortEvent<RenderLevelStageEvent.RegisterStageEvent> implements IPortModBusEvent {
        @Diff
        public PortRegisterStageEvent(RenderLevelStageEvent.RegisterStageEvent e) {
            super(e);
        }

        public PortStage register(ResourceLocation name, @Nullable RenderType renderType) throws IllegalArgumentException {
            return e.register(name, renderType).wrap();
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class PortStage {
        public static final PortStage AFTER_SKY = RenderLevelStageEvent.Stage.AFTER_SKY.wrap();
        public static final PortStage AFTER_SOLID_BLOCKS = RenderLevelStageEvent.Stage.AFTER_SOLID_BLOCKS.wrap();
        public static final PortStage AFTER_CUTOUT_MIPPED_BLOCKS_BLOCKS = RenderLevelStageEvent.Stage.AFTER_CUTOUT_MIPPED_BLOCKS_BLOCKS.wrap();
        public static final PortStage AFTER_CUTOUT_BLOCKS = RenderLevelStageEvent.Stage.AFTER_CUTOUT_BLOCKS.wrap();
        public static final PortStage AFTER_ENTITIES = RenderLevelStageEvent.Stage.AFTER_ENTITIES.wrap();
        public static final PortStage AFTER_BLOCK_ENTITIES = RenderLevelStageEvent.Stage.AFTER_BLOCK_ENTITIES.wrap();
        public static final PortStage AFTER_TRANSLUCENT_BLOCKS = RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS.wrap();
        public static final PortStage AFTER_TRIPWIRE_BLOCKS = RenderLevelStageEvent.Stage.AFTER_TRIPWIRE_BLOCKS.wrap();
        public static final PortStage AFTER_PARTICLES = RenderLevelStageEvent.Stage.AFTER_PARTICLES.wrap();
        public static final PortStage AFTER_WEATHER = RenderLevelStageEvent.Stage.AFTER_WEATHER.wrap();
        public static final PortStage AFTER_LEVEL = RenderLevelStageEvent.Stage.AFTER_LEVEL.wrap();

        private final RenderLevelStageEvent.Stage delegate;

        @Diff
        public PortStage(RenderLevelStageEvent.Stage delegate) {
            this.delegate = delegate;
        }

        @Override
        public String toString() {
            return delegate.toString();
        }

        @Override
        public boolean equals(Object obj) {
            return this == obj || obj instanceof PortStage stage && stage.delegate == delegate;
        }

        @Override
        public int hashCode() {
            return delegate.hashCode();
        }

        public static @Nullable PortRenderLevelStageEvent.PortStage fromRenderType(RenderType renderType) {
            return RenderLevelStageEvent.Stage.fromRenderType(renderType).wrap();
        }
    }
}
