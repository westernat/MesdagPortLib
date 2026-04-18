package org.mesdag.portlib.event.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.HumanoidArm;
import net.neoforged.neoforge.client.event.RenderArmEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortRenderArmEvent extends PortEvent<RenderArmEvent> {
    @Diff
    public PortRenderArmEvent(RenderArmEvent e) {
        super(e);
    }

    public HumanoidArm getArm() {
        return e.getArm();
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

    public AbstractClientPlayer getPlayer() {
        return e.getPlayer();
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
