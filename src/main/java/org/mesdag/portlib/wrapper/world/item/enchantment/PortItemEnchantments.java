package org.mesdag.portlib.wrapper.world.item.enchantment;

import it.unimi.dsi.fastutil.objects.AbstractObject2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.util.PortSets;

import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

@SuppressWarnings("all")
public class PortItemEnchantments {
    public static final PortItemEnchantments EMPTY = new PortItemEnchantments(new ListTag(), true);
    private final ListTag listTag;
    public final boolean showInTooltip;
    private Map<Enchantment, Integer> enchants;

    @Diff
    public PortItemEnchantments(ListTag listTag, boolean showInTooltip) {
        this.listTag = listTag;
        this.showInTooltip = showInTooltip;
    }

    @Diff
    public ListTag getListTag() {
        return listTag;
    }

    private Map<Enchantment, Integer> getEnchants() {
        if (enchants == null) {
            this.enchants = EnchantmentHelper.deserializeEnchantments(listTag);
        }
        return enchants;
    }

    public int getLevel(EnchantmentHolder enchantment) {
        return getEnchants().getOrDefault(enchantment.value(), 0);
    }

    public PortItemEnchantments withTooltip(boolean showInTooltip) {
        return new PortItemEnchantments(listTag, showInTooltip);
    }

    public Set<EnchantmentHolder> keySet() {
        return PortSets.immutableTransform(getEnchants().keySet(), EnchantmentHolder::wrap);
    }

    public Set<Object2IntMap.Entry<EnchantmentHolder>> entrySet() {
        return PortSets.immutableTransform(getEnchants().entrySet(), entry -> new AbstractObject2IntMap.BasicEntry<>(EnchantmentHolder.wrap(entry.getKey()), entry.getValue()));
    }

    public int size() {
        return getEnchants().size();
    }

    public boolean isEmpty() {
        return getEnchants().isEmpty();
    }

    public static boolean shouldGetStoredEnchantments(ItemStack stack) {
        return stack.is(Items.ENCHANTED_BOOK);
    }

    public static class Mutable {
        private final ListTag listTag;
        private final boolean showInTooltip;
        private Map<Enchantment, Integer> enchants;

        @Diff
        public Mutable(PortItemEnchantments immutable) {
            this.listTag = immutable.listTag;
            this.showInTooltip = immutable.showInTooltip;
        }

        private Map<Enchantment, Integer> getEnchants() {
            if (enchants == null) {
                this.enchants = EnchantmentHelper.deserializeEnchantments(listTag);
            }
            return enchants;
        }

        public void set(EnchantmentHolder enchantment, int level) {
            getEnchants().put(enchantment.value(), level);
        }

        public void upgrade(EnchantmentHolder enchantment, int level) {
            if (level <= 0) return;
            getEnchants().merge(enchantment.value(), Math.min(level, 255), Integer::max);
        }

        public void removeIf(Predicate<EnchantmentHolder> predicate) {
            getEnchants().keySet().removeIf(ench -> predicate.test(EnchantmentHolder.wrap(ench)));
        }

        public int getLevel(EnchantmentHolder enchantment) {
            return getEnchants().getOrDefault(enchantment.value(), 0);
        }

        public Set<EnchantmentHolder> keySet() {
            return PortSets.mutableTransform(getEnchants().keySet(), EnchantmentHolder::wrap, EnchantmentHolder::value);
        }

        public PortItemEnchantments toImmutable() {
            if (enchants == null) {
                return new PortItemEnchantments(listTag, showInTooltip);
            }
            ListTag listtag = new ListTag();
            for (Map.Entry<Enchantment, Integer> entry : enchants.entrySet()) {
                Enchantment enchantment = entry.getKey();
                if (enchantment != null) {
                    int i = entry.getValue();
                    listtag.add(EnchantmentHelper.storeEnchantment(EnchantmentHelper.getEnchantmentId(enchantment), i));
                }
            }
            return new PortItemEnchantments(listtag, showInTooltip);
        }
    }
}
