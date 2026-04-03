package org.mesdag.portlib.event.entity.player;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraftforge.event.entity.player.PlayerEvent;
import org.mesdag.portlib.diff.Diff;

import java.util.List;

public class PortPlayerEnchantItemEvent extends PlayerEvent {
    private final ItemStack enchantedItem;
    private final List<EnchantmentInstance> enchantments;

    @Diff
    public PortPlayerEnchantItemEvent(Player player, ItemStack enchantedItem, List<EnchantmentInstance> enchantments) {
        super(player);
        this.enchantedItem = enchantedItem;
        this.enchantments = enchantments;
    }

    public ItemStack getEnchantedItem() {
        return enchantedItem;
    }

    public List<EnchantmentInstance> getEnchantments() {
        return enchantments;
    }
}
