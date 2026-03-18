package org.mesdag.portlib.wrapper.world.item.enchantment;

import it.unimi.dsi.fastutil.objects.AbstractObject2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.util.ImmutableTransformSet;
import org.mesdag.portlib.util.PortSets;
import org.mesdag.portlib.wrapper.world.item.PortItemStack;

import java.util.Set;
import java.util.function.Predicate;

@SuppressWarnings("all")
public record PortItemEnchantments(ItemStack enchantedStack) {

    @Diff
    public ItemEnchantments unwrap() {
        return enchantedStack.getOrDefault(getType(enchantedStack), ItemEnchantments.EMPTY);
    }

    public PortItemEnchantments {
        if (enchantedStack.isEmpty()) {
            throw new IllegalArgumentException("EnchantedStack cannot be empty!");
        }
    }

    public int getLevel(EnchantmentHolder enchantment) {
        return unwrap().getLevel(enchantment);
    }

    public PortItemEnchantments withTooltip(boolean showInTooltip) {
        if (shouldGetStoredEnchantments(enchantedStack)) {
            PortItemStack.setShowStoredEnchantmentsTooltip(enchantedStack, showInTooltip);
        } else {
            PortItemStack.setShowEnchantmentsTooltip(enchantedStack, showInTooltip);
        }
        return this;
    }

    public Set<EnchantmentHolder> keySet() {
        return new ImmutableTransformSet<>(unwrap().keySet(), EnchantmentHolder::wrap);
    }

    public Set<Object2IntMap.Entry<EnchantmentHolder>> entrySet() {
        return new ImmutableTransformSet<>(unwrap().entrySet(), entry -> new AbstractObject2IntMap.BasicEntry<>(EnchantmentHolder.wrap(entry.getKey()), entry.getIntValue()));
    }

    public int size() {
        return unwrap().size();
    }

    public boolean isEmpty() {
        return unwrap().isEmpty();
    }

    public void applyTo(ItemStack stack) {
        if (stack == enchantedStack) return;
        stack.set(getType(stack), enchantedStack.get(getType(enchantedStack)));
    }

    public static boolean shouldGetStoredEnchantments(ItemStack stack) {
        return stack.is(Items.ENCHANTED_BOOK);
    }

    @Diff
    public static DataComponentType<ItemEnchantments> getType(ItemStack stack) {
        return shouldGetStoredEnchantments(stack) ? DataComponents.STORED_ENCHANTMENTS : DataComponents.ENCHANTMENTS;
    }

    public static class Mutable {
        private final ItemEnchantments.Mutable internal;

        public Mutable(ItemEnchantments.Mutable internal) {
            this.internal = internal;
        }

        public void set(EnchantmentHolder enchantment, int level) {
            internal.set(enchantment.delegate(), level);
        }

        public void upgrade(EnchantmentHolder enchantment, int level) {
            internal.upgrade(enchantment.delegate(), level);
        }

        public void removeIf(Predicate<EnchantmentHolder> predicate) {
            internal.removeIf(holder -> predicate.test(EnchantmentHolder.wrap(holder)));
        }

        public int getLevel(EnchantmentHolder enchantment) {
            return internal.getLevel(enchantment.delegate());
        }

        public Set<EnchantmentHolder> keySet() {
            return PortSets.mutableTransform(internal.keySet(), EnchantmentHolder::wrap, EnchantmentHolder::delegate);
        }

        public ItemEnchantments toImmutable() {
            return internal.toImmutable();
        }
    }
}
