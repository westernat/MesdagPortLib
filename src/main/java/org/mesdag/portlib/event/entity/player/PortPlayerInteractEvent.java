package org.mesdag.portlib.event.entity.player;

import PortLib.extensions.net.minecraftforge.eventbus.api.Event.PortEventExtension;
import PortLib.extensions.net.minecraftforge.fml.LogicalSide.PortLogicalSideExtension;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.wrapper.common.util.PortTriState;
import org.mesdag.portlib.wrapper.fml.PortLogicalSide;

public abstract class PortPlayerInteractEvent<E extends PlayerInteractEvent> extends PortPlayerEvent<E> {
    @Diff
    public PortPlayerInteractEvent(E e) {
        super(e);
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

    public PortLogicalSide getSide() {
        return PortLogicalSideExtension.wrap(e.getSide());
    }

    public static class EntityInteractSpecific extends PortPlayerInteractEvent<PlayerInteractEvent.EntityInteractSpecific> implements IPortCancellableEvent {
        @Diff
        public EntityInteractSpecific(PlayerInteractEvent.EntityInteractSpecific e) {
            super(e);
        }

        public Vec3 getLocalPos() {
            return e.getLocalPos();
        }

        public Entity getTarget() {
            return e.getTarget();
        }

        public InteractionResult getCancellationResult() {
            return e.getCancellationResult();
        }

        public void setCancellationResult(InteractionResult result) {
            e.setCancellationResult(result);
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class EntityInteract extends PortPlayerInteractEvent<PlayerInteractEvent.EntityInteract> implements IPortCancellableEvent {
        @Diff
        public EntityInteract(PlayerInteractEvent.EntityInteract e) {
            super(e);
        }

        public Entity getTarget() {
            return e.getTarget();
        }

        public InteractionResult getCancellationResult() {
            return e.getCancellationResult();
        }

        public void setCancellationResult(InteractionResult result) {
            e.setCancellationResult(result);
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class RightClickBlock extends PortPlayerInteractEvent<PlayerInteractEvent.RightClickBlock> implements IPortCancellableEvent {
        @Diff
        public RightClickBlock(PlayerInteractEvent.RightClickBlock e) {
            super(e);
        }

        public PortTriState getUseBlock() {
            return PortEventExtension.Result.wrap(e.getUseBlock());
        }

        public PortTriState getUseItem() {
            return PortEventExtension.Result.wrap(e.getUseItem());
        }

        public BlockHitResult getHitVec() {
            return e.getHitVec();
        }

        public void setUseBlock(PortTriState triggerBlock) {
            e.setUseBlock(triggerBlock.unwrapResult());
        }

        public void setUseItem(PortTriState triggerItem) {
            e.setUseItem(triggerItem.unwrapResult());
        }

        public InteractionResult getCancellationResult() {
            return e.getCancellationResult();
        }

        public void setCancellationResult(InteractionResult result) {
            e.setCancellationResult(result);
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class RightClickItem extends PortPlayerInteractEvent<PlayerInteractEvent.RightClickItem> implements IPortCancellableEvent {
        @Diff
        public RightClickItem(PlayerInteractEvent.RightClickItem e) {
            super(e);
        }

        public InteractionResult getCancellationResult() {
            return e.getCancellationResult();
        }

        public void setCancellationResult(InteractionResult result) {
            e.setCancellationResult(result);
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class RightClickEmpty extends PortPlayerInteractEvent<PlayerInteractEvent.RightClickEmpty> implements IPortCancellableEvent {
        @Diff
        public RightClickEmpty(PlayerInteractEvent.RightClickEmpty e) {
            super(e);
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class LeftClickBlock extends PortPlayerInteractEvent<PlayerInteractEvent.LeftClickBlock> implements IPortCancellableEvent {
        @Diff
        public LeftClickBlock(PlayerInteractEvent.LeftClickBlock e) {
            super(e);
        }

        public PortTriState getUseBlock() {
            return PortEventExtension.Result.wrap(e.getUseBlock());
        }

        public PortTriState getUseItem() {
            return PortEventExtension.Result.wrap(e.getUseItem());
        }

        public PortAction getAction() {
            return PortAction.wrap(e.getAction());
        }

        public void setUseBlock(PortTriState triggerBlock) {
            e.setUseBlock(triggerBlock.unwrapResult());
        }

        public void setUseItem(PortTriState triggerItem) {
            e.setUseItem(triggerItem.unwrapResult());
        }

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

            @Diff
            public static PortAction wrap(PlayerInteractEvent.LeftClickBlock.Action action) {
                return switch (action) {
                    case START -> START;
                    case STOP -> STOP;
                    case ABORT -> ABORT;
                    case CLIENT_HOLD -> CLIENT_HOLD;
                };
            }
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class LeftClickEmpty extends PortPlayerInteractEvent<PlayerInteractEvent.LeftClickEmpty> implements IPortCancellableEvent {
        @Diff
        public LeftClickEmpty(PlayerInteractEvent.LeftClickEmpty e) {
            super(e);
        }

        static {
            PortEventHooks.register();
        }
    }
}
