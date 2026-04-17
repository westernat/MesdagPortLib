package org.mesdag.portlib.event.entity.client.event;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.event.RenderHandEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortRenderHandEvent extends PortEvent {
    private final RenderHandEvent e;

    @Diff
    public PortRenderHandEvent(RenderHandEvent e) {
        super(e);
        this.e = e;
    }

    public InteractionHand getHand() {
        return e.getHand();
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

    public float getInterpolatedPitch() {
        return e.getInterpolatedPitch();
    }

    public float getSwingProgress() {
        return e.getSwingProgress();
    }

    public float getEquipProgress() {
        return e.getEquipProgress();
    }

    public ItemStack getItemStack() {
        return e.getItemStack();
    }

    public void setCanceled(boolean canceled) {
        e.setCanceled(canceled);
    }

    public boolean isCanceled() {
        return e.isCanceled();
    }

    static {
        PortEventHooks.register(RenderHandEvent.class, PortRenderHandEvent.class, PortRenderHandEvent::new);
    }
}
