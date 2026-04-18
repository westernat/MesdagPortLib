package org.mesdag.portlib.event.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.event.RenderBlockScreenEffectEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortRenderBlockScreenEffectEvent extends PortEvent<RenderBlockScreenEffectEvent> implements IPortCancellableEvent {
    @Diff
    public PortRenderBlockScreenEffectEvent(RenderBlockScreenEffectEvent e) {
        super(e);
    }

    public Player getPlayer() {
        return e.getPlayer();
    }

    public PoseStack getPoseStack() {
        return e.getPoseStack();
    }

    public PortOverlayType getOverlayType() {
        return e.getOverlayType().wrap();
    }

    public BlockState getBlockState() {
        return e.getBlockState();
    }

    public BlockPos getBlockPos() {
        return e.getBlockPos();
    }

    static {
        PortEventHooks.register();
    }

    public enum PortOverlayType {
        FIRE,
        BLOCK,
        WATER;

        @Diff
        public RenderBlockScreenEffectEvent.OverlayType unwrap() {
            if (this == FIRE) {
                return RenderBlockScreenEffectEvent.OverlayType.FIRE;
            } else if (this == BLOCK) {
                return RenderBlockScreenEffectEvent.OverlayType.BLOCK;
            }
            return RenderBlockScreenEffectEvent.OverlayType.WATER;
        }
    }
}
