package org.mesdag.portlib.event.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraftforge.client.event.RenderPlayerEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.event.entity.player.PortPlayerEvent;

public abstract class PortRenderPlayerEvent<E extends RenderPlayerEvent> extends PortPlayerEvent<E> {
    @Diff
    public PortRenderPlayerEvent(E e) {
        super(e);
    }

    public PlayerRenderer getRenderer() {
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

    public static class Pre extends PortRenderPlayerEvent<RenderPlayerEvent.Pre> implements IPortCancellableEvent {
        @Diff
        public Pre(RenderPlayerEvent.Pre e) {
            super(e);
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class Post extends PortRenderPlayerEvent<RenderPlayerEvent.Post> {
        @Diff
        public Post(RenderPlayerEvent.Post e) {
            super(e);
        }

        static {
            PortEventHooks.register();
        }
    }
}
