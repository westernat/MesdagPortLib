package org.mesdag.portlib.event.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemFrameRenderer;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RenderItemInFrameEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortRenderItemInFrameEvent extends PortEvent<RenderItemInFrameEvent> {
    @Diff
    public PortRenderItemInFrameEvent(RenderItemInFrameEvent e) {
        super(e);
    }

    public ItemStack getItemStack() {
        return e.getItemStack();
    }

    public ItemFrame getItemFrameEntity() {
        return e.getItemFrameEntity();
    }

    public ItemFrameRenderer<?> getRenderer() {
        return e.getRenderer();
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

    public void setCanceled(boolean canceled) {
        e.setCanceled(canceled);
    }

    public boolean isCanceled() {
        return e.isCanceled();
    }

    static {
        PortEventHooks.register();
    }
}
