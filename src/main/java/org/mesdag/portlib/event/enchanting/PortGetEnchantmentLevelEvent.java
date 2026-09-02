package org.mesdag.portlib.event.enchanting;

import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHandler;
import org.mesdag.portlib.wrapper.common.extensions.IPortHolderExtension;
import org.mesdag.portlib.wrapper.world.item.enchantment.EnchantmentHolder;
import org.mesdag.portlib.wrapper.world.item.enchantment.PortItemEnchantments;

import java.util.Optional;

public class PortGetEnchantmentLevelEvent extends Event {
    protected final ItemStack stack;
    protected final PortItemEnchantments.Mutable enchantments;
    protected final @Nullable EnchantmentHolder targetEnchant;
    protected final HolderLookup.RegistryLookup<Enchantment> lookup;

    @Diff
    public PortGetEnchantmentLevelEvent(ItemStack stack, PortItemEnchantments.Mutable enchantments, @Nullable EnchantmentHolder targetEnchant, HolderLookup.RegistryLookup<Enchantment> lookup) {
        this.stack = stack;
        this.enchantments = enchantments;
        this.targetEnchant = targetEnchant;
        this.lookup = lookup;
    }

    public ItemStack getStack() {
        return stack;
    }

    public PortItemEnchantments.Mutable getEnchantments() {
        return enchantments;
    }

    public @Nullable EnchantmentHolder getTargetEnchant() {
        return targetEnchant;
    }

    public boolean isTargetting(EnchantmentHolder ench) {
        return targetEnchant == ench;
    }

    public boolean isTargetting(ResourceKey<Enchantment> ench) {
        return targetEnchant == null || targetEnchant.is(ench);
    }

    public Optional<EnchantmentHolder> getEnchantmentHolder(ResourceKey<Enchantment> key) {
        return lookup.get(key).map(EnchantmentHolder::wrap);
    }

    public int getLevel(EnchantmentHolder enchantment) {
        return enchantments.getLevel(enchantment);
    }

    public void setLevel(EnchantmentHolder enchantment, int level) {
        enchantments.set(enchantment, level);
    }

    public HolderLookup.RegistryLookup<Enchantment> getLookup() {
        return lookup;
    }

    @Diff
    public static int getEnchantmentLevelSpecific(int level, ItemStack stack, EnchantmentHolder ench) {
        HolderLookup.RegistryLookup<Enchantment> lookup = IPortHolderExtension.of(ench).unwrapLookup();
        if (lookup == null) {
            return level;
        }

        var enchantments = new PortItemEnchantments.Mutable(PortItemEnchantments.EMPTY);
        enchantments.set(ench, level);
        var event = new PortGetEnchantmentLevelEvent(stack, enchantments, ench, lookup);
        PortEventHandler.postEvent(event);
        return enchantments.getLevel(ench);
    }

    @Diff
    public static PortItemEnchantments getAllEnchantmentLevels(PortItemEnchantments enchantments, ItemStack stack, HolderLookup.RegistryLookup<Enchantment> lookup) {
        var mutableEnchantments = new PortItemEnchantments.Mutable(enchantments);
        var event = new PortGetEnchantmentLevelEvent(stack, mutableEnchantments, null, lookup);
        PortEventHandler.postEvent(event);
        return mutableEnchantments.toImmutable();
    }
}
