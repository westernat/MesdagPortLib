package org.mesdag.portlib.event;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.AnvilUpdateEvent;
import org.mesdag.portlib.diff.Diff;

import javax.annotation.Nullable;

public class PortAnvilUpdateEvent extends PortEvent implements IPortCancellableEvent {
    private final AnvilUpdateEvent e;

    @Diff
    public PortAnvilUpdateEvent(AnvilUpdateEvent e) {
        super();
        this.e = e;
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