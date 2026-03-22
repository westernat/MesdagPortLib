package org.mesdag.portlib.event;

import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.ItemStackedOnOtherEvent;
import org.mesdag.portlib.diff.Diff;


public class PortItemStackedOnOtherEvent extends PortEvent implements IPortCancellableEvent {

    private final ItemStackedOnOtherEvent e;

    @Diff
    public PortItemStackedOnOtherEvent(ItemStackedOnOtherEvent e) {
        this.e = e;
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
        PortEventHooks.register(ItemStackedOnOtherEvent.class, PortItemStackedOnOtherEvent.class, PortItemStackedOnOtherEvent::new);
    }
}