package org.mesdag.portlib.event.other;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.GrindstoneEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public abstract class PortGrindstoneEvent<E extends GrindstoneEvent> extends PortEvent<E> {
    @Diff
    public PortGrindstoneEvent(E e) {
        super(e);
    }

    public ItemStack getTopItem() {
        return e.getTopItem();
    }

    public ItemStack getBottomItem() {
        return e.getBottomItem();
    }

    public int getXp() {
        return e.getXp();
    }

    public void setXp(int xp) {
        e.setXp(xp);
    }

    public static class OnPlaceItem extends PortGrindstoneEvent<GrindstoneEvent.OnPlaceItem> implements IPortCancellableEvent {
        @Diff
        public OnPlaceItem(GrindstoneEvent.OnPlaceItem e) {
            super(e);
        }

        public ItemStack getOutput() {
            return e.getOutput();
        }

        public void setOutput(ItemStack output) {
            e.setOutput(output);
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class OnTakeItem extends PortGrindstoneEvent<GrindstoneEvent.OnTakeItem> implements IPortCancellableEvent {
        @Diff
        public OnTakeItem(GrindstoneEvent.OnTakeItem e) {
            super(e);
        }

        public ItemStack getNewTopItem() {
            return e.getNewTopItem();
        }

        public ItemStack getNewBottomItem() {
            return e.getNewBottomItem();
        }

        public void setNewTopItem(ItemStack newTop) {
            e.setNewTopItem(newTop);
        }

        public void setNewBottomItem(ItemStack newBottom) {
            e.setNewBottomItem(newBottom);
        }

//  todo      public ContainerLevelAccess getContainerAccess() {
//            return e.getContainerAccess();
//        }
//
//        public @Nullable Player getPlayer() {
//            return e.getPlayer();
//        }

        static {
            PortEventHooks.register();
        }
    }
}
