package org.mesdag.portlib.event.entity.player;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.neoforged.neoforge.event.entity.player.PlayerEnchantItemEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;

import java.util.List;

public class PortPlayerEnchantItemEvent extends PortPlayerEvent {
    private final PlayerEnchantItemEvent e;

    @Diff
    public PortPlayerEnchantItemEvent(PlayerEnchantItemEvent e) {
        super(e.getEntity());
        this.e = e;
    }

    public ItemStack getEnchantedItem() {
        return e.getEnchantedItem();
    }

    public List<EnchantmentInstance> getEnchantments() {
        return e.getEnchantments();
    }

    static {
        PortEventHooks.register(PlayerEnchantItemEvent.class, PortPlayerEnchantItemEvent.class, PortPlayerEnchantItemEvent::new);
    }
}
