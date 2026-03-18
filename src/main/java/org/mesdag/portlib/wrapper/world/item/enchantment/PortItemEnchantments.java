package org.mesdag.portlib.wrapper.world.item.enchantment;

import it.unimi.dsi.fastutil.objects.AbstractObject2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.util.PortSets;

import java.util.Set;
import java.util.function.Predicate;

@SuppressWarnings("all")
public class PortItemEnchantments {
    private final ItemEnchantments delegate;
    public final boolean showInTooltip;

    @Diff
    public PortItemEnchantments(ItemEnchantments delegate) {
        this.delegate = delegate;
        this.showInTooltip = delegate.showInTooltip;
    }

    @Diff
    public ItemEnchantments unwrap() {
        return delegate;
    }

    public int getLevel(EnchantmentHolder enchantment) {
        return delegate.getLevel(enchantment.delegate());
    }

    public PortItemEnchantments withTooltip(boolean showInTooltip) {
        return new PortItemEnchantments(delegate.withTooltip(showInTooltip));
    }

    public Set<EnchantmentHolder> keySet() {
        return PortSets.immutableTransform(delegate.keySet(), EnchantmentHolder::wrap);
    }

    public Set<Object2IntMap.Entry<EnchantmentHolder>> entrySet() {
        return PortSets.immutableTransform(delegate.entrySet(), entry -> new AbstractObject2IntMap.BasicEntry<>(EnchantmentHolder.wrap(entry.getKey()), entry.getIntValue()));
    }

    public int size() {
        return delegate.size();
    }

    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    public static boolean shouldGetStoredEnchantments(ItemStack stack) {
        return stack.is(Items.ENCHANTED_BOOK);
    }

    public static class PortMutable {
        private final ItemEnchantments.Mutable delegate;

        @Diff
        public PortMutable(ItemEnchantments.Mutable delegate) {
            this.delegate = delegate;
        }

        public void set(EnchantmentHolder enchantment, int level) {
            delegate.set(enchantment.delegate(), level);
        }

        public void upgrade(EnchantmentHolder enchantment, int level) {
            delegate.upgrade(enchantment.delegate(), level);
        }

        public void removeIf(Predicate<EnchantmentHolder> predicate) {
            delegate.removeIf(holder -> predicate.test(EnchantmentHolder.wrap(holder)));
        }

        public int getLevel(EnchantmentHolder enchantment) {
            return delegate.getLevel(enchantment.delegate());
        }

        public Set<EnchantmentHolder> keySet() {
            return PortSets.mutableTransform(delegate.keySet(), EnchantmentHolder::wrap, EnchantmentHolder::delegate);
        }

        public PortItemEnchantments toImmutable() {
            return new PortItemEnchantments(delegate.toImmutable());
        }
    }
}
