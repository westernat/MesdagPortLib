package org.mesdag.portlib.wrapper.world.item.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public class PortEnchantmentHelper {
    @FunctionalInterface
    public interface PortEnchantmentVisitor {
        void accept(Enchantment enchantment, int level);
    }

    @FunctionalInterface
    public interface PortEnchantmentSlotVisitor {
        void accept(Enchantment enchantment, int level, ItemStack stack, EquipmentSlot slot, LivingEntity entity);
    }
}
