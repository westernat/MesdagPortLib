package org.mesdag.portlib.event.other;

import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.ItemStackedOnOtherEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

/// 修复了forge的一个bug：carried与stackedOn搞反了
public class PortItemStackedOnOtherEvent extends PortEvent<ItemStackedOnOtherEvent> implements IPortCancellableEvent {
    @Diff
    public PortItemStackedOnOtherEvent(ItemStackedOnOtherEvent e) {
        super(e);
    }

    public ItemStack getCarriedItem() {
        return e.getStackedOnItem(); // 修复
    }

    public ItemStack getStackedOnItem() {
        return e.getCarriedItem(); // 修复
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
