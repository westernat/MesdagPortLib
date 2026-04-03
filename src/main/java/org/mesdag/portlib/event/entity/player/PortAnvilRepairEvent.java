package org.mesdag.portlib.event.entity.player;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;

public class PortAnvilRepairEvent extends PortPlayerEvent<AnvilRepairEvent> {
    @Diff
    public PortAnvilRepairEvent(AnvilRepairEvent e) {
        super(e);
    }

    public ItemStack getOutput() {
        return e.getOutput();
    }

    public ItemStack getLeft() {
        return e.getLeft();
    }

    public ItemStack getRight() {
        return e.getRight();
    }

    public float getBreakChance() {
        return e.getBreakChance();
    }

    public void setBreakChance(float breakChance) {
        e.setBreakChance(breakChance);
    }

    static {
        PortEventHooks.register(AnvilRepairEvent.class, PortAnvilRepairEvent.class, PortAnvilRepairEvent::new);
    }
}
