package org.mesdag.portlib.wrapper.world.item.enchantment;

import it.unimi.dsi.fastutil.objects.AbstractObject2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.mesdag.portlib.util.TransformSet;
import org.mesdag.portlib.wrapper.world.item.PortItemStack;

import java.util.Set;

@SuppressWarnings("all")
public record PortItemEnchantments(ItemStack enchantedStack) {
    public PortItemEnchantments {
        if (enchantedStack.isEmpty()) {
            throw new IllegalArgumentException("EnchantedStack cannot be empty!");
        }
    }

    public int getLevel(EnchantmentHolder enchantment) {
        ListTag enchantments = getEnchantmentTags(enchantedStack);
        ResourceLocation targetId = EnchantmentHelper.getEnchantmentId(enchantment.value());

        for (int i = 0; i < enchantments.size(); ++i) {
            CompoundTag tag = enchantments.getCompound(i);
            ResourceLocation id = EnchantmentHelper.getEnchantmentId(tag);
            if (id != null && id.equals(targetId)) {
                return EnchantmentHelper.getEnchantmentLevel(tag);
            }
        }

        return 0;
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
        return new TransformSet<>(EnchantmentHelper.getEnchantments(enchantedStack).keySet(), EnchantmentHolder::wrap);
    }

    public Set<Object2IntMap.Entry<EnchantmentHolder>> entrySet() {
        return new TransformSet<>(EnchantmentHelper.getEnchantments(enchantedStack).entrySet(), entry -> new AbstractObject2IntMap.BasicEntry<>(EnchantmentHolder.wrap(entry.getKey()), entry.getValue()));
    }

    public int size() {
        return EnchantmentHelper.getEnchantments(enchantedStack).size();
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public void applyTo(ItemStack stack) {
        if (stack == enchantedStack) return;
        if (shouldGetStoredEnchantments(stack)) {
            CompoundTag tag = enchantedStack.getTag();
            if (tag != null && tag.contains(EnchantedBookItem.TAG_STORED_ENCHANTMENTS, Tag.TAG_LIST)) {
                stack.getOrCreateTag().put(
                        EnchantedBookItem.TAG_STORED_ENCHANTMENTS,
                        tag.getList(EnchantedBookItem.TAG_STORED_ENCHANTMENTS, Tag.TAG_COMPOUND).copy()
                );
            }
        } else {
            CompoundTag tag = stack.getTag();
            if (tag != null && tag.contains(ItemStack.TAG_ENCH, Tag.TAG_LIST)) {
                stack.getOrCreateTag().put(
                        ItemStack.TAG_ENCH,
                        tag.getList(ItemStack.TAG_ENCH, Tag.TAG_COMPOUND).copy()
                );
            }
        }
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
}
