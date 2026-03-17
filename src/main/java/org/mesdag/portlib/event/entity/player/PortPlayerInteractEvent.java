package org.mesdag.portlib.event.entity.player;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.wrapper.PortLogicalSide;
import org.mesdag.portlib.wrapper.common.util.PortTriState;

public abstract class PortPlayerInteractEvent extends PortPlayerEvent {
    private final PlayerInteractEvent e;

    @Diff
    public PortPlayerInteractEvent(PlayerInteractEvent e) {
        this.e = e;
    }

    @Override
    public Player getEntity() {
        return e.getEntity();
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
        return PortLogicalSide.wrap(e.getSide());
    }

    public abstract static class PortEntityInteractSpecific extends PortPlayerInteractEvent implements IPortCancellableEvent {
        @Diff
        public PortEntityInteractSpecific(PlayerInteractEvent e) {
            super(e);
        }

        public abstract Vec3 getLocalPos();

        public abstract Entity getTarget();

        public abstract InteractionResult getCancellationResult();

        public abstract void setCancellationResult(InteractionResult result);

        static {
            PortEventHooks.register(PlayerInteractEvent.EntityInteractSpecific.class, PortEntityInteractSpecific.class, e -> new PortEntityInteractSpecific(e) {
                @Override
                public Vec3 getLocalPos() {
                    return e.getLocalPos();
                }

                @Override
                public Entity getTarget() {
                    return e.getTarget();
                }

                @Override
                public InteractionResult getCancellationResult() {
                    return e.getCancellationResult();
                }

                @Override
                public void setCancellationResult(InteractionResult result) {
                    e.setCancellationResult(result);
                }
            });
        }
    }

    public abstract static class PortEntityInteract extends PortPlayerInteractEvent implements IPortCancellableEvent {
        @Diff
        public PortEntityInteract(PlayerInteractEvent e) {
            super(e);
        }

        public abstract Entity getTarget();

        public abstract InteractionResult getCancellationResult();

        public abstract void setCancellationResult(InteractionResult result);

        static {
            PortEventHooks.register(PlayerInteractEvent.EntityInteract.class, PortEntityInteract.class, e -> new PortEntityInteract(e) {
                @Override
                public Entity getTarget() {
                    return e.getTarget();
                }

                @Override
                public InteractionResult getCancellationResult() {
                    return e.getCancellationResult();
                }

                @Override
                public void setCancellationResult(InteractionResult result) {
                    e.setCancellationResult(result);
                }
            });
        }
    }

    public abstract static class PortRightClickBlock extends PortPlayerInteractEvent implements IPortCancellableEvent {
        @Diff
        public PortRightClickBlock(PlayerInteractEvent e) {
            super(e);
        }

        public abstract PortTriState getUseBlock();

        public abstract PortTriState getUseItem();

        public abstract BlockHitResult getHitVec();

        public abstract void setUseBlock(PortTriState triggerBlock);

        public abstract void setUseItem(PortTriState triggerItem);

        @Override
        public abstract void setCanceled(boolean canceled);

        public abstract InteractionResult getCancellationResult();

        public abstract void setCancellationResult(InteractionResult result);

        static {
            PortEventHooks.register(PlayerInteractEvent.RightClickBlock.class, PortRightClickBlock.class, e -> new PortRightClickBlock(e) {
                @Override
                public PortTriState getUseBlock() {
                    return PortTriState.wrap(e.getUseBlock());
                }

                @Override
                public PortTriState getUseItem() {
                    return PortTriState.wrap(e.getUseBlock());
                }

                @Override
                public BlockHitResult getHitVec() {
                    return e.getHitVec();
                }

                @Override
                public void setUseBlock(PortTriState triggerBlock) {
                    e.setUseBlock(triggerBlock.unwrap());
                }

                @Override
                public void setUseItem(PortTriState triggerItem) {
                    e.setUseItem(triggerItem.unwrap());
                }

                @Override
                public void setCanceled(boolean canceled) {
                    e.setCanceled(canceled);
                }

                @Override
                public InteractionResult getCancellationResult() {
                    return e.getCancellationResult();
                }

                @Override
                public void setCancellationResult(InteractionResult result) {
                    e.setCancellationResult(result);
                }
            });
        }
    }

    public abstract static class PortRightClickItem extends PortPlayerInteractEvent implements IPortCancellableEvent {
        @Diff
        public PortRightClickItem(PlayerInteractEvent e) {
            super(e);
        }

        public abstract InteractionResult getCancellationResult();

        public abstract void setCancellationResult(InteractionResult result);

        static {
            PortEventHooks.register(PlayerInteractEvent.RightClickItem.class, PortRightClickItem.class, e -> new PortRightClickItem(e) {
                @Override
                public InteractionResult getCancellationResult() {
                    return e.getCancellationResult();
                }

                @Override
                public void setCancellationResult(InteractionResult result) {
                    e.setCancellationResult(result);
                }
            });
        }
    }

    public static class PortRightClickEmpty extends PortPlayerInteractEvent {
        @Diff
        public PortRightClickEmpty(PlayerInteractEvent e) {
            super(e);
        }

        static {
            PortEventHooks.register(PlayerInteractEvent.RightClickEmpty.class, PortRightClickEmpty.class, PortRightClickEmpty::new);
        }
    }

    public abstract static class PortLeftClickBlock extends PortPlayerInteractEvent implements IPortCancellableEvent {
        @Diff
        public PortLeftClickBlock(PlayerInteractEvent e) {
            super(e);
        }

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
            PortEventHooks.register(PlayerInteractEvent.LeftClickBlock.class, PortLeftClickBlock.class, e -> new PortLeftClickBlock(e) {
                @Override
                public PortTriState getUseBlock() {
                    return PortTriState.wrap(e.getUseBlock());
                }

                @Override
                public PortTriState getUseItem() {
                    return PortTriState.wrap(e.getUseItem());
                }

                @Override
                public PortAction getAction() {
                    return PortAction.wrap(e.getAction());
                }

                @Override
                public void setUseBlock(PortTriState triggerBlock) {
                    e.setUseBlock(triggerBlock.unwrap());
                }

                @Override
                public void setUseItem(PortTriState triggerItem) {
                    e.setUseItem(triggerItem.unwrap());
                }

                @Override
                public void setCanceled(boolean canceled) {
                    e.setCanceled(canceled);
                }
            });
        }
    }

    public static class PortLeftClickEmpty extends PortPlayerInteractEvent {
        @Diff
        public PortLeftClickEmpty(PlayerInteractEvent e) {
            super(e);
        }

        static {
            PortEventHooks.register(PlayerInteractEvent.LeftClickEmpty.class, PortLeftClickEmpty.class, PortLeftClickEmpty::new);
        }
    }
}
