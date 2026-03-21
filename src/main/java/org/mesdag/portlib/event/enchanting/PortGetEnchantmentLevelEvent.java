package org.mesdag.portlib.event.enchanting;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.neoforge.event.enchanting.GetEnchantmentLevelEvent;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.wrapper.world.item.enchantment.EnchantmentHolder;
import org.mesdag.portlib.wrapper.world.item.enchantment.PortItemEnchantments;

import java.util.Optional;

public class PortGetEnchantmentLevelEvent extends PortEvent {
    private final GetEnchantmentLevelEvent e;

    @Diff
    public PortGetEnchantmentLevelEvent(GetEnchantmentLevelEvent e) {
        this.e = e;
    }

    public ItemStack getStack() {
        return e.getStack();
    }

    public PortItemEnchantments.PortMutable getEnchantments() {
        return new PortItemEnchantments.PortMutable(e.getEnchantments());
    }

    public @Nullable EnchantmentHolder getTargetEnchant() {
        Holder<Enchantment> target = e.getTargetEnchant();
        return target == null ? null : EnchantmentHolder.wrap(target);
    }

    public boolean isTargetting(EnchantmentHolder ench) {
        return e.isTargetting(ench.delegate());
    }

    public boolean isTargetting(ResourceKey<Enchantment> ench) {
        return e.isTargetting(ench);
    }

    public Optional<EnchantmentHolder> getEnchantmentHolder(ResourceKey<Enchantment> key) {
        return e.getLookup().get(key).map(EnchantmentHolder::wrap);
    }

    public int getLevel(EnchantmentHolder enchantment) {
        return e.getEnchantments().getLevel(enchantment.delegate());
    }

    public void setLevel(EnchantmentHolder enchantment, int level) {
        e.getEnchantments().set(enchantment.delegate(), level);
    }

    public HolderLookup.RegistryLookup<Enchantment> getLookup() {
        return e.getLookup();
    }

    static {
        PortEventHooks.register(GetEnchantmentLevelEvent.class, PortGetEnchantmentLevelEvent.class, PortGetEnchantmentLevelEvent::new);
    }
}
