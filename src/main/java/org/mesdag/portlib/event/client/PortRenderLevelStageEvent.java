package org.mesdag.portlib.event.client;

import PortLib.extensions.net.minecraftforge.client.event.RenderLevelStageEvent.PortRenderLevelStageEventExtension;
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

    public Stage getStage() {
        return PortRenderLevelStageEventExtension.Stage.wrap(e.getStage());
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

    public static class RegisterStageEvent extends PortEvent<RenderLevelStageEvent.RegisterStageEvent> implements IPortModBusEvent {
        @Diff
        public RegisterStageEvent(RenderLevelStageEvent.RegisterStageEvent e) {
            super(e);
        }

        public Stage register(ResourceLocation name, @Nullable RenderType renderType) throws IllegalArgumentException {
            return PortRenderLevelStageEventExtension.Stage.wrap(e.register(name, renderType));
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class Stage {
        public static final Stage AFTER_SKY = PortRenderLevelStageEventExtension.Stage.wrap(RenderLevelStageEvent.Stage.AFTER_SKY);
        public static final Stage AFTER_SOLID_BLOCKS = PortRenderLevelStageEventExtension.Stage.wrap(RenderLevelStageEvent.Stage.AFTER_SOLID_BLOCKS);
        public static final Stage AFTER_CUTOUT_MIPPED_BLOCKS_BLOCKS = PortRenderLevelStageEventExtension.Stage.wrap(RenderLevelStageEvent.Stage.AFTER_CUTOUT_MIPPED_BLOCKS_BLOCKS);
        public static final Stage AFTER_CUTOUT_BLOCKS = PortRenderLevelStageEventExtension.Stage.wrap(RenderLevelStageEvent.Stage.AFTER_CUTOUT_BLOCKS);
        public static final Stage AFTER_ENTITIES = PortRenderLevelStageEventExtension.Stage.wrap(RenderLevelStageEvent.Stage.AFTER_ENTITIES);
        public static final Stage AFTER_BLOCK_ENTITIES = PortRenderLevelStageEventExtension.Stage.wrap(RenderLevelStageEvent.Stage.AFTER_BLOCK_ENTITIES);
        public static final Stage AFTER_TRANSLUCENT_BLOCKS = PortRenderLevelStageEventExtension.Stage.wrap(RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS);
        public static final Stage AFTER_TRIPWIRE_BLOCKS = PortRenderLevelStageEventExtension.Stage.wrap(RenderLevelStageEvent.Stage.AFTER_TRIPWIRE_BLOCKS);
        public static final Stage AFTER_PARTICLES = PortRenderLevelStageEventExtension.Stage.wrap(RenderLevelStageEvent.Stage.AFTER_PARTICLES);
        public static final Stage AFTER_WEATHER = PortRenderLevelStageEventExtension.Stage.wrap(RenderLevelStageEvent.Stage.AFTER_WEATHER);
        public static final Stage AFTER_LEVEL = PortRenderLevelStageEventExtension.Stage.wrap(RenderLevelStageEvent.Stage.AFTER_LEVEL);

        private final RenderLevelStageEvent.Stage delegate;

        @Diff
        public Stage(RenderLevelStageEvent.Stage delegate) {
            this.delegate = delegate;
        }

        @Override
        public String toString() {
            return delegate.toString();
        }

        @Override
        public boolean equals(Object obj) {
            return this == obj || obj instanceof Stage stage && stage.delegate == delegate;
        }

        @Override
        public int hashCode() {
            return delegate.hashCode();
        }

        public static @Nullable PortRenderLevelStageEvent.Stage fromRenderType(RenderType renderType) {
            return PortRenderLevelStageEventExtension.Stage.wrap(RenderLevelStageEvent.Stage.fromRenderType(renderType));
        }
    }
}
