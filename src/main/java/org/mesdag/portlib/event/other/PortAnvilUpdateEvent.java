package org.mesdag.portlib.event.other;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.AnvilUpdateEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

import javax.annotation.Nullable;

public class PortAnvilUpdateEvent extends PortEvent<AnvilUpdateEvent> implements IPortCancellableEvent {
    @Diff
    public PortAnvilUpdateEvent(AnvilUpdateEvent e) {
        super(e);
    }

    public ItemStack getLeft() {
        return e.getLeft();
    }

    public ItemStack getRight() {
        return e.getRight();
    }

    public @Nullable String getName() {
        return e.getName();
    }

    public ItemStack getOutput() {
        return e.getOutput();
    }

    public void setOutput(ItemStack output) {
        e.setOutput(output);
    }

    public long getCost() {
        return e.getCost();
    }

    public void setCost(long cost) {
        e.setCost(cost);
    }

    public int getMaterialCost() {
        return e.getMaterialCost();
    }

    public void setMaterialCost(int materialCost) {
        e.setMaterialCost(materialCost);
    }

    public Player getPlayer() {
        return e.getPlayer();
    }

    static {
        PortEventHooks.register(AnvilUpdateEvent.class, PortAnvilUpdateEvent.class, PortAnvilUpdateEvent::new);
    }
}