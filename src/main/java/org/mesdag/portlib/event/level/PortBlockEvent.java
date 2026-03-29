package org.mesdag.portlib.event.level;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.PortalShape;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.common.util.BlockSnapshot;
import net.neoforged.neoforge.event.level.BlockEvent;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

import java.util.EnumSet;
import java.util.List;

public abstract class PortBlockEvent<E extends BlockEvent> extends PortEvent<E> {
    @Diff
    public PortBlockEvent(E e) {
        super(e);
    }

    public LevelAccessor getLevel() {
        return e.getLevel();
    }

    public BlockPos getPos() {
        return e.getPos();
    }

    public BlockState getState() {
        return e.getState();
    }

    public static class PortBreakEvent extends PortBlockEvent<BlockEvent.BreakEvent> implements IPortCancellableEvent {
        @Diff
        public PortBreakEvent(BlockEvent.BreakEvent e) {
            super(e);
        }

        public Player getPlayer() {
            return e.getPlayer();
        }

        static {
            PortEventHooks.register(BlockEvent.BreakEvent.class, PortBreakEvent.class, PortBreakEvent::new);
        }
    }

    public static class PortEntityPlaceEvent extends PortBlockEvent<BlockEvent.EntityPlaceEvent> implements IPortCancellableEvent {
        @Diff
        public PortEntityPlaceEvent(BlockEvent.EntityPlaceEvent e) {
            super(e);
        }

        @Nullable
        public Entity getEntity() {
            return e.getEntity();
        }

        public BlockSnapshot getBlockSnapshot() {
            return e.getBlockSnapshot();
        }

        public BlockState getPlacedBlock() {
            return e.getPlacedBlock();
        }

        public BlockState getPlacedAgainst() {
            return e.getPlacedAgainst();
        }

        static {
            PortEventHooks.register(BlockEvent.EntityPlaceEvent.class, PortEntityPlaceEvent.class, PortEntityPlaceEvent::new);
        }
    }

    public static class PortEntityMultiPlaceEvent extends PortEntityPlaceEvent implements IPortCancellableEvent {
        private final List<BlockSnapshot> blockSnapshots;

        @Diff
        public PortEntityMultiPlaceEvent(BlockEvent.EntityMultiPlaceEvent e) {
            super(e);
            this.blockSnapshots = e.getReplacedBlockSnapshots();
        }

        public List<BlockSnapshot> getReplacedBlockSnapshots() {
            return blockSnapshots;
        }

        static {
            PortEventHooks.register(BlockEvent.EntityMultiPlaceEvent.class, PortEntityMultiPlaceEvent.class, PortEntityMultiPlaceEvent::new);
        }
    }

    public static class PortNeighborNotifyEvent extends PortBlockEvent<BlockEvent.NeighborNotifyEvent> implements IPortCancellableEvent {
        @Diff
        public PortNeighborNotifyEvent(BlockEvent.NeighborNotifyEvent e) {
            super(e);
        }

        public EnumSet<Direction> getNotifiedSides() {
            return e.getNotifiedSides();
        }

        public boolean getForceRedstoneUpdate() {
            return e.getForceRedstoneUpdate();
        }

        static {
            PortEventHooks.register(BlockEvent.NeighborNotifyEvent.class, PortNeighborNotifyEvent.class, PortNeighborNotifyEvent::new);
        }
    }

    public static class PortFluidPlaceBlockEvent extends PortBlockEvent<BlockEvent.FluidPlaceBlockEvent> implements IPortCancellableEvent {
        @Diff
        public PortFluidPlaceBlockEvent(BlockEvent.FluidPlaceBlockEvent e) {
            super(e);
        }

        public BlockPos getLiquidPos() {
            return e.getLiquidPos();
        }

        public BlockState getNewState() {
            return e.getNewState();
        }

        public void setNewState(BlockState state) {
            e.setNewState(state);
        }

        public BlockState getOriginalState() {
            return e.getOriginalState();
        }

        static {
            PortEventHooks.register(BlockEvent.FluidPlaceBlockEvent.class, PortFluidPlaceBlockEvent.class, PortFluidPlaceBlockEvent::new);
        }
    }

    public static class PortFarmlandTrampleEvent extends PortBlockEvent<BlockEvent.FarmlandTrampleEvent> implements IPortCancellableEvent {
        @Diff
        public PortFarmlandTrampleEvent(BlockEvent.FarmlandTrampleEvent e) {
            super(e);
        }

        public Entity getEntity() {
            return e.getEntity();
        }

        public float getFallDistance() {
            return e.getFallDistance();
        }

        static {
            PortEventHooks.register(BlockEvent.FarmlandTrampleEvent.class, PortFarmlandTrampleEvent.class, PortFarmlandTrampleEvent::new);
        }
    }

    public static class PortalSpawnEvent extends PortBlockEvent<BlockEvent.PortalSpawnEvent> implements IPortCancellableEvent {
        @Diff
        public PortalSpawnEvent(BlockEvent.PortalSpawnEvent e) {
            super(e);
        }

        public PortalShape getPortalSize() {
            return e.getPortalSize();
        }

        static {
            PortEventHooks.register(BlockEvent.PortalSpawnEvent.class, PortalSpawnEvent.class, PortalSpawnEvent::new);
        }
    }

    public static class PortBlockToolModificationEvent extends PortBlockEvent<BlockEvent.BlockToolModificationEvent> implements ICancellableEvent {
        @Diff
        public PortBlockToolModificationEvent(BlockEvent.BlockToolModificationEvent e) {
            super(e);
        }

        public @Nullable Player getPlayer() {
            return e.getPlayer();
        }

        public ItemStack getHeldItemStack() {
            return e.getHeldItemStack();
        }

        public ItemAbility getItemAbility() {
            return e.getItemAbility();
        }

        public boolean isSimulated() {
            return e.isSimulated();
        }

        public UseOnContext getContext() {
            return e.getContext();
        }

        public void setFinalState(@Nullable BlockState finalState) {
            e.setFinalState(finalState);
        }

        public BlockState getFinalState() {
            return e.getFinalState();
        }

        static {
            PortEventHooks.register(BlockEvent.BlockToolModificationEvent.class, PortBlockToolModificationEvent.class, PortBlockToolModificationEvent::new);
        }
    }
}
