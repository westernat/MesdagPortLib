package org.mesdag.portlib.wrapper.world.item.enchantment;

import it.unimi.dsi.fastutil.objects.AbstractObject2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.util.PortSets;

import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

@SuppressWarnings("all")
public class PortItemEnchantments {
    private final ListTag enchants;
    public final boolean showInTooltip;

    @Diff
    public PortItemEnchantments(ListTag enchants, boolean showInTooltip) {
        this.enchants = enchants;
        this.showInTooltip = showInTooltip;
    }

    @Diff
    public ListTag getEnchants() {
        return enchants;
    }

    public int getLevel(EnchantmentHolder enchantment) {
        ResourceLocation targetId = EnchantmentHelper.getEnchantmentId(enchantment.value());

        for (int i = 0; i < enchants.size(); ++i) {
            CompoundTag tag = enchants.getCompound(i);
            ResourceLocation id = EnchantmentHelper.getEnchantmentId(tag);
            if (id != null && id.equals(targetId)) {
                return EnchantmentHelper.getEnchantmentLevel(tag);
            }
        }

        return 0;
    }

    public PortItemEnchantments withTooltip(boolean showInTooltip) {
        return new PortItemEnchantments(enchants, showInTooltip);
    }

    public Set<EnchantmentHolder> keySet() {
        return PortSets.immutableTransform(EnchantmentHelper.deserializeEnchantments(enchants).keySet(), EnchantmentHolder::wrap);
    }

    public Set<Object2IntMap.Entry<EnchantmentHolder>> entrySet() {
        return PortSets.immutableTransform(EnchantmentHelper.deserializeEnchantments(enchants).entrySet(), entry -> new AbstractObject2IntMap.BasicEntry<>(EnchantmentHolder.wrap(entry.getKey()), entry.getValue()));
    }

    public int size() {
        return enchants.size();
    }

    public boolean isEmpty() {
        return enchants.isEmpty();
    }

    public static boolean shouldGetStoredEnchantments(ItemStack stack) {
        return stack.is(Items.ENCHANTED_BOOK);
    }

    private static ListTag getEnchantmentTags(ItemStack stack) {
        if (shouldGetStoredEnchantments(stack)) {
            return EnchantedBookItem.getEnchantments(stack);
        }
        return stack.getEnchantmentTags();
    }

    private static int getSize(ItemStack stack) {
        return getEnchantmentTags(stack).stream().filter(tag -> {
            ResourceLocation id = EnchantmentHelper.getEnchantmentId((CompoundTag) tag);
            return ForgeRegistries.ENCHANTMENTS.containsKey(id);
        }).mapToInt(t -> 1).sum();
    }

    public static class PortMutable {
        private final ListTag listTag;
        private Map<Enchantment, Integer> enchants;
        private final boolean showInTooltip;

        @Diff
        public PortMutable(PortItemEnchantments immutable) {
            this.listTag = immutable.enchants;
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
