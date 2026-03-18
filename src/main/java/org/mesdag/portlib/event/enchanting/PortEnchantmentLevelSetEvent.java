package org.mesdag.portlib.event.enchanting;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.enchanting.EnchantmentLevelSetEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortEnchantmentLevelSetEvent extends PortEvent {
    private final EnchantmentLevelSetEvent e;

    @Diff
    public PortEnchantmentLevelSetEvent(EnchantmentLevelSetEvent e) {
        this.e = e;
    }

    public Level getLevel() {
        return e.getLevel();
    }

    public BlockPos getPos() {
        return e.getPos();
    }

    public int getEnchantRow() {
        return e.getEnchantRow();
    }

    public int getPower() {
        return e.getPower();
    }

    public ItemStack getItem() {
        return e.getItem();
    }

    public int getOriginalLevel() {
        return e.getOriginalLevel();
    }

    public int getEnchantLevel() {
        return e.getEnchantLevel();
    }

    public void setEnchantLevel(int level) {
        e.setEnchantLevel(level);
    }

    static {
        PortEventHooks.register(EnchantmentLevelSetEvent.class, PortEnchantmentLevelSetEvent.class, PortEnchantmentLevelSetEvent::new);
    }
}
