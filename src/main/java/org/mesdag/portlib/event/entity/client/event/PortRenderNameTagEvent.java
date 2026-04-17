package org.mesdag.portlib.event.entity.client.event;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.client.event.RenderNameTagEvent;
import net.neoforged.neoforge.common.util.TriState;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortRenderNameTagEvent extends PortEvent {
    private final RenderNameTagEvent e;

    @Diff
    public PortRenderNameTagEvent(RenderNameTagEvent e) {
        super(e);
        this.e = e;
    }

    public Entity getEntity() {
        return e.getEntity();
    }

    public Component getOriginalContent() {
        return e.getOriginalContent();
    }

    public EntityRenderer<?> getEntityRenderer() {
        return e.getEntityRenderer();
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

    public float getPartialTick() {
        return e.getPartialTick();
    }

    public Component getContent() {
        return e.getContent();
    }

    public void setContent(Component contents) {
        e.setContent(contents);
    }

    public TriState canRender() {
        return e.canRender();
    }

    public void setCanRender(TriState canRender) {
        e.setCanRender(canRender);
    }

    static {
        PortEventHooks.register(RenderNameTagEvent.class, PortRenderNameTagEvent.class, PortRenderNameTagEvent::new);
    }
}