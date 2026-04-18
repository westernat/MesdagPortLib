package org.mesdag.portlib.event.entity.player;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.neoforged.neoforge.event.entity.player.PlayerEnchantItemEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;

import java.util.List;

public class PortPlayerEnchantItemEvent extends PortPlayerEvent<PlayerEnchantItemEvent> {
    @Diff
    public PortPlayerEnchantItemEvent(PlayerEnchantItemEvent e) {
        super(e);
    }

    public ItemStack getEnchantedItem() {
        return e.getEnchantedItem();
    }

    public List<EnchantmentInstance> getEnchantments() {
        return e.getEnchantments();
    }

    static {
        PortEventHooks.register();
    }
}
