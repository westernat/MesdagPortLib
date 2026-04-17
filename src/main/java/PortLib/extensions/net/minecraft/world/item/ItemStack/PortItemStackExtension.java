package PortLib.extensions.net.minecraft.world.item.ItemStack;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.mesdag.portlib.event.enchanting.PortGetEnchantmentLevelEvent;
import org.mesdag.portlib.wrapper.world.item.PortItemStack;
import org.mesdag.portlib.wrapper.world.item.enchantment.EnchantmentHolder;
import org.mesdag.portlib.wrapper.world.item.enchantment.PortItemEnchantments;

@Extension
public class PortItemStackExtension {
    public static int getEnchantmentLevel(@This ItemStack thiz, EnchantmentHolder enchantment) {
        int level = thiz.getEnchantmentLevel(enchantment.value());
        return PortGetEnchantmentLevelEvent.getEnchantmentLevelSpecific(level, thiz, enchantment);
    }

    public static PortItemEnchantments getAllPortEnchantments(@This ItemStack thiz, HolderLookup.RegistryLookup<Enchantment> lookup) {
        var enchantments = PortItemStack.getEnchantments(thiz);
        return PortGetEnchantmentLevelEvent.getAllEnchantmentLevels(enchantments, thiz, lookup);
    }
}
