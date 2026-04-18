package org.mesdag.portlib.event.other;

import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.ItemStackedOnOtherEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;


public class PortItemStackedOnOtherEvent extends PortEvent<ItemStackedOnOtherEvent> implements IPortCancellableEvent {
    @Diff
    public PortItemStackedOnOtherEvent(ItemStackedOnOtherEvent e) {
        super(e);
    }

    public ItemStack getCarriedItem() {
        return e.getCarriedItem();
    }

    public ItemStack getStackedOnItem() {
        return e.getStackedOnItem();
    }

    public Slot getSlot() {
        return e.getSlot();
    }

    public ClickAction getClickAction() {
        return e.getClickAction();
    }

    public Player getPlayer() {
        return e.getPlayer();
    }

    public SlotAccess getCarriedSlotAccess() {
        return e.getCarriedSlotAccess();
    }

    static {
        PortEventHooks.register();
    }
}
