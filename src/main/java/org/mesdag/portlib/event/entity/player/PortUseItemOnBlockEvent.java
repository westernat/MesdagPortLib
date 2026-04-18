package org.mesdag.portlib.event.entity.player;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.entity.player.UseItemOnBlockEvent;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.wrapper.fml.PortLogicalSide;

public class PortUseItemOnBlockEvent extends PortEvent<UseItemOnBlockEvent> implements IPortCancellableEvent {
    @Diff
    public PortUseItemOnBlockEvent(UseItemOnBlockEvent e) {
        super(e);
    }

    public @Nullable Player getPlayer() {
        return e.getPlayer();
    }

    public InteractionHand getHand() {
        return e.getHand();
    }

    public ItemStack getItemStack() {
        return e.getItemStack();
    }

    public BlockPos getPos() {
        return e.getPos();
    }

    public @Nullable Direction getFace() {
        return e.getFace();
    }

    public Level getLevel() {
        return e.getLevel();
    }

    public UseOnContext getUseOnContext() {
        return e.getUseOnContext();
    }

    public PortUsePhase getUsePhase() {
        return PortUsePhase.wrap(e.getUsePhase());
    }

    public PortLogicalSide getSide() {
        return e.getSide().wrap();
    }

    public void cancelWithResult(ItemInteractionResult result) {
        e.cancelWithResult(result);
        setCanceled(true);
    }

    public ItemInteractionResult getCancellationResult() {
        return e.getCancellationResult();
    }

    public void setCancellationResult(ItemInteractionResult result) {
        e.setCancellationResult(result);
    }

    public enum PortUsePhase {
        ITEM_BEFORE_BLOCK,
        BLOCK,
        ITEM_AFTER_BLOCK;

        @Diff
        public UseItemOnBlockEvent.UsePhase unwrap() {
            return switch (this) {
                case ITEM_BEFORE_BLOCK -> UseItemOnBlockEvent.UsePhase.ITEM_BEFORE_BLOCK;
                case BLOCK -> UseItemOnBlockEvent.UsePhase.BLOCK;
                case ITEM_AFTER_BLOCK -> UseItemOnBlockEvent.UsePhase.ITEM_AFTER_BLOCK;
            };
        }

        @Diff
        public static PortUsePhase wrap(UseItemOnBlockEvent.UsePhase phase) {
            return switch (phase) {
                case ITEM_BEFORE_BLOCK -> ITEM_BEFORE_BLOCK;
                case BLOCK -> BLOCK;
                case ITEM_AFTER_BLOCK -> ITEM_AFTER_BLOCK;
            };
        }
    }

    static {
        PortEventHooks.register();
    }
}
