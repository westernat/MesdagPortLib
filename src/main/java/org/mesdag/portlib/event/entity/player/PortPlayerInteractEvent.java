package org.mesdag.portlib.event.entity.player;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.wrapper.PortLogicalSide;
import org.mesdag.portlib.wrapper.common.util.PortTriState;

public abstract class PortPlayerInteractEvent extends PortPlayerEvent {
    public abstract static class PortEntityInteractSpecific extends PortPlayerInteractEvent implements IPortCancellableEvent {
        public abstract Vec3 getLocalPos();

        public abstract Entity getTarget();

        public abstract InteractionResult getCancellationResult();

        public abstract void setCancellationResult(InteractionResult result);
    }

    public abstract static class PortEntityInteract extends PortPlayerInteractEvent implements IPortCancellableEvent {
        public abstract Entity getTarget();

        public abstract InteractionResult getCancellationResult();

        public abstract void setCancellationResult(InteractionResult result);
    }

    public abstract static class PortRightClickBlock extends PortPlayerInteractEvent implements IPortCancellableEvent {
        public abstract TriState getUseBlock();

        public abstract TriState getUseItem();

        public abstract BlockHitResult getHitVec();

        public abstract void setUseBlock(TriState triggerBlock);

        public abstract void setUseItem(TriState triggerItem);

        @Override
        public abstract void setCanceled(boolean canceled);

        public abstract InteractionResult getCancellationResult();

        public abstract void setCancellationResult(InteractionResult result);
    }

    public abstract static class PortRightClickItem extends PortPlayerInteractEvent implements IPortCancellableEvent {
        public abstract InteractionResult getCancellationResult();

        public abstract void setCancellationResult(InteractionResult result);
    }

    public abstract static class PortRightClickEmpty extends PortPlayerInteractEvent {}

    public abstract static class PortLeftClickBlock extends PortPlayerInteractEvent implements IPortCancellableEvent {
        public abstract PortTriState getUseBlock();

        public abstract PortTriState getUseItem();

        public abstract PortAction getAction();

        public abstract void setUseBlock(PortTriState triggerBlock);

        public abstract void setUseItem(PortTriState triggerItem);

        @Override
        public abstract void setCanceled(boolean canceled);

        public enum PortAction {
            START,
            STOP,
            ABORT,
            CLIENT_HOLD;

            @Diff
            public PlayerInteractEvent.LeftClickBlock.Action unwrap() {
                return switch (this) {
                    case START -> PlayerInteractEvent.LeftClickBlock.Action.START;
                    case STOP -> PlayerInteractEvent.LeftClickBlock.Action.STOP;
                    case ABORT -> PlayerInteractEvent.LeftClickBlock.Action.ABORT;
                    case CLIENT_HOLD -> PlayerInteractEvent.LeftClickBlock.Action.CLIENT_HOLD;
                };
            }
        }
    }

    public abstract static class PortLeftClickEmpty extends PortPlayerInteractEvent {}

    public abstract InteractionHand getHand();

    public abstract ItemStack getItemStack();

    public abstract BlockPos getPos();

    public abstract @Nullable Direction getFace();

    public abstract Level getLevel();

    public abstract PortLogicalSide getSide();
}
