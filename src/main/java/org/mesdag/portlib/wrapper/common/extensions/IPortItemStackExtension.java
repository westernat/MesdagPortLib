package org.mesdag.portlib.wrapper.common.extensions;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.enchanting.PortGetEnchantmentLevelEvent;
import org.mesdag.portlib.wrapper.PortSelfGetter;
import org.mesdag.portlib.wrapper.world.item.PortItemStack;
import org.mesdag.portlib.wrapper.world.item.enchantment.EnchantmentHolder;
import org.mesdag.portlib.wrapper.world.item.enchantment.PortItemEnchantments;

public interface IPortItemStackExtension extends PortSelfGetter<ItemStack> {
    default int getEnchantmentLevel(EnchantmentHolder enchantment) {
        int level = portlib$self().getEnchantmentLevel(enchantment.value());
        return PortGetEnchantmentLevelEvent.getEnchantmentLevelSpecific(level, portlib$self(), enchantment);
    }

    default PortItemEnchantments getAllEnchantments(HolderLookup.RegistryLookup<Enchantment> lookup) {
        var enchantments = PortItemStack.getEnchantments(portlib$self());
        return PortGetEnchantmentLevelEvent.getAllEnchantmentLevels(enchantments, portlib$self(), lookup);
    }

    static IPortItemStackExtension of(ItemStack stack) {
        return ((Object) stack) instanceof IPortItemStackExtension extensions
                ? extensions
                : new Delegate(stack);
    }

    @Diff
    record Delegate(ItemStack delegate) implements IPortItemStackExtension {
        @Override
        public ItemStack portlib$self() {
            return delegate;
        }
    }
}
