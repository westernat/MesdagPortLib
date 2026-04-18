package org.mesdag.portlib.event.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.client.event.RenderNameTagEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.wrapper.common.util.PortTriState;

public class PortRenderNameTagEvent extends PortEvent<RenderNameTagEvent> {
    @Diff
    public PortRenderNameTagEvent(RenderNameTagEvent e) {
        super(e);
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

    public PortTriState canRender() {
        return e.getResult().wrap();
    }

    public void setCanRender(PortTriState canRender) {
        e.setResult(canRender.unwrapResult());
    }

    static {
        PortEventHooks.register();
    }
}
