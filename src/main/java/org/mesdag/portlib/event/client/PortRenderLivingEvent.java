package org.mesdag.portlib.event.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public abstract class PortRenderLivingEvent<T extends LivingEntity, M extends EntityModel<T>, E extends RenderLivingEvent<T, M>> extends PortEvent<E> {
    @Diff
    public PortRenderLivingEvent(E e) {
        super(e);
    }

    public LivingEntity getEntity() {
        return e.getEntity();
    }

    public LivingEntityRenderer<T, M> getRenderer() {
        return e.getRenderer();
    }

    public float getPartialTick() {
        return e.getPartialTick();
    }

    public PoseStack getPoseStack() {
        return e.getPoseStack();
    }

    public MultiBufferSource getMultiBufferSource() {
        return e.getMultiBufferSource();
    }

    public int getPackedLight() {
        return e.getPackedLight();
    }

    public static class PortPre<T extends LivingEntity, M extends EntityModel<T>> extends PortRenderLivingEvent<T, M, RenderLivingEvent.Pre<T, M>> implements IPortCancellableEvent {
        @Diff
        public PortPre(RenderLivingEvent.Pre<T, M> e) {
            super(e);
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class PortPost<T extends LivingEntity, M extends EntityModel<T>> extends PortRenderLivingEvent<T, M, RenderLivingEvent.Post<T, M>> {
        @Diff
        public PortPost(RenderLivingEvent.Post<T, M> e) {
            super(e);
        }

        static {
            PortEventHooks.register();
        }
    }
}
